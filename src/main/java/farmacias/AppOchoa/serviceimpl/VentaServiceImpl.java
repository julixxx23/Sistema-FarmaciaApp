package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.venta.VentaCreateDTO;
import farmacias.AppOchoa.dto.venta.VentaResponseDTO;
import farmacias.AppOchoa.dto.venta.VentaSimpleDTO;
import farmacias.AppOchoa.dto.venta.VentaUpdateDTO;
import farmacias.AppOchoa.dto.ventadetalle.VentaDetalleCreateDTO;
import farmacias.AppOchoa.model.*;
import farmacias.AppOchoa.repository.*;
import farmacias.AppOchoa.services.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioLotesRepository loteRepository;

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            SucursalRepository sucursalRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            InventarioLotesRepository loteRepository) {
        this.ventaRepository = ventaRepository;
        this.sucursalRepository = sucursalRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    @Override
    public VentaResponseDTO crear(VentaCreateDTO dto) {
        // 1. Validaciones de Entidades Principales
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convierte Compra
        Venta venta = Venta.builder()
                .sucursal(sucursal)
                .usuario(usuario)
                .ventaNitCliente(dto.getNitCliente() != null ? dto.getNitCliente() : "CF")
                .ventaNombreCliente(dto.getNombreCliente() != null ? dto.getNombreCliente() : "Consumidor Final")
                .ventaEstado(VentaEstado.completada)
                .detalles(new ArrayList<>())
                .build();

        BigDecimal acumuladorSubtotal = BigDecimal.ZERO;

        // 3. Procesar Detalles y Actualizar Inventario
        for (VentaDetalleCreateDTO detalleDto : dto.getDetalles()) {

            Producto producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detalleDto.getProductoId()));

            InventarioLotes lote = loteRepository.findById(detalleDto.getLoteId())
                    .orElseThrow(() -> new RuntimeException("Lote no encontrado ID: " + detalleDto.getLoteId()));

            // VALIDACIÓN DE STOCK
            if (lote.getLoteCantidadActual() < detalleDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente en el lote: " + lote.getLoteNumero());
            }

            // DESCUENTO DE INVENTARIO
            lote.setLoteCantidadActual(lote.getLoteCantidadActual() - detalleDto.getCantidad());
            loteRepository.save(lote);

            // Cálculos monetarios
            BigDecimal subtotalLinea = detalleDto.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDto.getCantidad()));
            acumuladorSubtotal = acumuladorSubtotal.add(subtotalLinea);

            // Crear el objeto detalle
            VentaDetalle detalle = VentaDetalle.builder()
                    .producto(producto)
                    .lote(lote)
                    .venta(venta)
                    .detalleCantidad(detalleDto.getCantidad())
                    .detallePrecioUnitario(detalleDto.getPrecioUnitario())
                    .detalleSubtotal(subtotalLinea)
                    .build();

            venta.getDetalles().add(detalle);
        }

        //Totales Finales
        venta.setVentaSubtotal(acumuladorSubtotal);
        BigDecimal descuento = dto.getDescuento() != null ? dto.getDescuento() : BigDecimal.ZERO;
        venta.setVentaDescuento(descuento);
        venta.setVentaTotal(acumuladorSubtotal.subtract(descuento));

        //Guardar (Se guarda cabecera y detalles por el CascadeType.ALL)
        Venta guardada = ventaRepository.save(venta);

        return VentaResponseDTO.fromEntity(guardada);
    }

    @Override
    public VentaResponseDTO listarPorId(Long id){
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada por ID: " +id));

        return VentaResponseDTO.fromEntity(venta);
    }

    @Override
    public List<VentaSimpleDTO> listarTodas(){
        List<Venta> ventas = ventaRepository.findAll();

        return ventas.stream()
                .map(VentaSimpleDTO:: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaSimpleDTO> listarActivas(){
        List<Venta> ventas = ventaRepository.findByVentaEstado(VentaEstado.completada);

        return ventas.stream()
                .map(VentaSimpleDTO:: fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VentaResponseDTO actualizar(Long id, VentaUpdateDTO dto){
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada por ID: " + id));


        // actualizar datos como el nombre del cliente o NIT si hubo error
        if(dto.getNombreCliente() != null) venta.setVentaNombreCliente(dto.getNombreCliente());
        if(dto.getNitCliente() != null) venta.setVentaNitCliente(dto.getNitCliente());

        Venta guardar = ventaRepository.save(venta);
        return VentaResponseDTO.fromEntity(guardar);
    }

    @Override
    public void cambiarEstado(Long id, VentaEstado nuevoEstado){
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada por ID: " +id));

        // ANULAR una venta que estaba COMPLETADA
        if(nuevoEstado == VentaEstado.anulada && venta.getVentaEstado() == VentaEstado.completada){
            for(VentaDetalle detalle : venta.getDetalles()){
                InventarioLotes lote = detalle.getLote();

                // Devolvemos el stock al lote
                lote.setLoteCantidadActual(lote.getLoteCantidadActual()+ detalle.getDetalleCantidad());

                // Si el lote estaba agotado, ahora tiene stock, así que vuelve a estar disponible
                if(lote.getLoteEstado() == LoteEstado.agotado){
                    lote.setLoteEstado(LoteEstado.disponible);
                }

                loteRepository.save(lote);
            }

        }
        // Actualizamos el estado de la venta
        venta.setVentaEstado(nuevoEstado);
        ventaRepository.save(venta);

    }

    @Override
    public  void eliminar(Long id){
        cambiarEstado(id, VentaEstado.anulada);
    }












}