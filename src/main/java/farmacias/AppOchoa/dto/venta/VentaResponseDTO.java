package farmacias.AppOchoa.dto.venta;

import farmacias.AppOchoa.dto.sucursal.SucursalSimpleDTO;
import farmacias.AppOchoa.dto.usuario.UsuarioSimpleDTO;
import farmacias.AppOchoa.dto.ventadetalle.VentaDetalleResponseDTO;
import farmacias.AppOchoa.model.Venta;
import farmacias.AppOchoa.model.VentaEstado;
import farmacias.AppOchoa.model.VentaFel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaResponseDTO {

    private Long ventaId;


    private String uuid;
    private String numeroAutorizacion;
    private String serie;
    private String numeroFactura;
    private LocalDateTime fechaCertificacion;
    private String felEstado;
    private String nitCliente;
    private String nombreCliente;
    private SucursalSimpleDTO sucursal;
    private UsuarioSimpleDTO usuario;
    private LocalDateTime fechaVenta;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private VentaEstado estado;
    private LocalDateTime fechaCreacion;

    private List<VentaDetalleResponseDTO> detalles;

    public static VentaResponseDTO fromEntity(Venta venta) {
        if (venta == null) return null;

        VentaFel fel = venta.getVentaFel();

        return VentaResponseDTO.builder()
                .ventaId(venta.getVentaId())
                .uuid(fel != null ? fel.getFelUuid() : null)
                .numeroAutorizacion(fel != null ? fel.getFelNumeroAutorizacion() : null)
                .fechaCertificacion(fel != null ? fel.getFelFechaCertificacion() : null)
                .felEstado(fel != null ? fel.getFelEstado().name() : null)
                .serie(venta.getVentaSerie())
                .numeroFactura(venta.getVentaNumeroFactura())
                .nitCliente(venta.getVentaNitCliente())
                .nombreCliente(venta.getVentaNombreCliente())
                .sucursal(venta.getSucursal() != null ?
                        SucursalSimpleDTO.fromEntity(venta.getSucursal()) : null)
                .usuario(venta.getUsuario() != null ?
                        UsuarioSimpleDTO.fromEntity(venta.getUsuario()) : null)
                .fechaVenta(venta.getVentaFecha())
                .subtotal(venta.getVentaSubtotal())
                .descuento(venta.getVentaDescuento())
                .total(venta.getVentaTotal())
                .estado(venta.getVentaEstado())
                .fechaCreacion(venta.getAuditoriaFechaCreacion())
                .detalles(venta.getDetalles() != null ?
                        venta.getDetalles().stream()
                                .map(VentaDetalleResponseDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}