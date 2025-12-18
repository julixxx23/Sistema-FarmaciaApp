package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.alerta.AlertaCreateDTO; // Asegúrate de crear este DTO
import farmacias.AppOchoa.dto.alerta.AlertaResponseDTO;
import farmacias.AppOchoa.dto.alerta.AlertaSimpleDTO;
import farmacias.AppOchoa.dto.alerta.AlertaUpdateDTO;
import farmacias.AppOchoa.model.Alerta;
import farmacias.AppOchoa.model.InventarioLotes;
import farmacias.AppOchoa.model.Producto;
import farmacias.AppOchoa.model.Sucursal;
import farmacias.AppOchoa.repository.*;
import farmacias.AppOchoa.services.AlertaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final InventarioLotesRepository inventarioLotesRepository;

    public AlertaServiceImpl(
            AlertaRepository alertaRepository,
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository,
            InventarioLotesRepository inventarioLotesRepository) {
        this.alertaRepository = alertaRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.inventarioLotesRepository = inventarioLotesRepository;
    }

    //Método para crear alertas (útil para el sistema o el admin)
    @Override
    public AlertaResponseDTO crear(AlertaCreateDTO dto) {
        Producto producto = buscarProducto(dto.getProductoId());
        Sucursal sucursal = buscarSucursal(dto.getSucursalId());

        // El lote es opcional en algunas alertas (ej. stock general de sucursal)
        InventarioLotes lote = null;
        if (dto.getLoteId() != null) {
            lote = buscarInventarioLotes(dto.getLoteId());
        }

        Alerta alerta = Alerta.builder()
                .alertaMensaje(dto.getMensaje())
                .alertaLeida(false)
                .producto(producto)
                .sucursal(sucursal)
                .lote(lote)
                .build();

        return AlertaResponseDTO.fromEntity(alertaRepository.save(alerta));
    }

    @Override
    public AlertaResponseDTO listarPorId(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada ID: " + id));
        return AlertaResponseDTO.fromEntity(alerta);
    }

    @Override
    public List<AlertaSimpleDTO> listarTodas() {
        return alertaRepository.findAll().stream()
                .map(AlertaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertaSimpleDTO> listarNoLeidas() {
        return alertaRepository.findByAlertaLeidaFalse().stream()
                .map(AlertaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AlertaResponseDTO actualizar(Long id, AlertaUpdateDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada ID: " + id));

        if (dto.getLeida() != null) {
            alerta.setAlertaLeida(dto.getLeida());
        }

        return AlertaResponseDTO.fromEntity(alertaRepository.save(alerta));
    }

    @Override
    public void cambiarEstado(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada ID: " + id));
        // Alternar estado: si es true pasa a false, si es false pasa a true
        alerta.setAlertaLeida(!alerta.getAlertaLeida());
        alertaRepository.save(alerta);
    }

    @Override
    public void eliminar(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new RuntimeException("Alerta no encontrada ID: " + id);
        }
        alertaRepository.deleteById(id);
    }

    // Métodos auxiliares privados para evitar repetición de código
    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + id));
    }

    private Sucursal buscarSucursal(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada ID: " + id));
    }

    private InventarioLotes buscarInventarioLotes(Long id) {
        return inventarioLotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado ID: " + id));
    }
}