-- =============================================================================
-- M9 — Backfill de inventario agregado
-- =============================================================================
-- Recalcula inventario.inventario_cantidad_actual a partir de la suma real de
-- inventario_lotes.lote_cantidad_actual por producto+sucursal.
--
-- Contexto: hasta el fix M9, la tabla `inventario` (cantidad agregada que
-- consultan las alertas de stock bajo) nunca se sincronizaba con los movimientos
-- reales de lotes (ventas/compras). Las filas existentes están desincronizadas.
-- El fix corrige de aquí en adelante; este script corrige los datos históricos.
--
-- EJECUCIÓN MANUAL. No se corre automáticamente (el proyecto no usa Flyway/Liquibase).
--
-- ANTES DE EJECUTAR:
--   1. Hacer backup de la tabla `inventario`.
--   2. Ejecutar en una ventana sin ventas/compras en curso, o dentro de una
--      transacción, para que la foto de lotes sea consistente.
--   3. Revisar primero el SELECT de diagnóstico para ver qué filas cambiarán.
-- =============================================================================

-- 1) Diagnóstico: filas cuyo agregado no coincide con la suma de lotes.
--    (Ejecutar y revisar antes de aplicar el UPDATE.)
SELECT  i.inventario_id,
        i.producto_id,
        i.sucursal_id,
        i.inventario_cantidad_actual                          AS actual_guardado,
        COALESCE(SUM(l.lote_cantidad_actual), 0)              AS suma_real_lotes,
        COALESCE(SUM(l.lote_cantidad_actual), 0)
            - i.inventario_cantidad_actual                    AS diferencia
FROM        inventario i
LEFT JOIN   inventario_lotes l
       ON   l.producto_id = i.producto_id
      AND   l.sucursal_id = i.sucursal_id
GROUP BY    i.inventario_id, i.producto_id, i.sucursal_id, i.inventario_cantidad_actual
HAVING      i.inventario_cantidad_actual <> COALESCE(SUM(l.lote_cantidad_actual), 0);

-- 2) Backfill: fija el agregado igual a la suma de lotes del mismo producto+sucursal.
--    Los inventarios sin ningún lote quedan en 0 (vía la subconsulta correlacionada).
START TRANSACTION;

UPDATE inventario i
SET i.inventario_cantidad_actual = (
        SELECT COALESCE(SUM(l.lote_cantidad_actual), 0)
        FROM   inventario_lotes l
        WHERE  l.producto_id = i.producto_id
          AND  l.sucursal_id = i.sucursal_id
);

-- Revisar el número de filas afectadas y, si es correcto:
COMMIT;
-- En caso de duda: ROLLBACK;

-- NOTA: este backfill cubre las filas `inventario` ya existentes. Los pares
-- producto+sucursal que tienen lotes pero NO tienen fila en `inventario`
-- (caso que el fix M9 ahora crea al comprar) no se materializan aquí; si se
-- quieren crear retroactivamente, hacerlo en un script aparte tras validar la
-- cantidad mínima deseada por producto.
