package farmacias.AppOchoa.serviceimpl;

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
public class AlertaServiceImpl implements AlertaService{
    private final AlertaRepository alertaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final InventarioLotesRepository inventarioLotesRepository;

    public AlertaServiceImpl(
            AlertaRepository alertaRepository,
            ProductoRepository productoRepository,
            SucursalRepository sucursalRepository,
            InventarioLotesRepository inventarioLotesRepository){
        this.alertaRepository = alertaRepository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.inventarioLotesRepository = inventarioLotesRepository;
    }

    @Override
    public AlertaResponseDTO listarPorId(Long id){
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada por ID: " +id));

        return AlertaResponseDTO.fromEntity(alerta);
    }

    @Override
    public List<AlertaSimpleDTO> listarTodas() {
        List<Alerta> alertas = alertaRepository.findAll();

        return alertas.stream()
                .map(AlertaSimpleDTO::fromEntity)
                .collect(Collectors.toList());

    }


    @Override
    public List<AlertaSimpleDTO> listarNoLeidas(){
        List<Alerta> alertas = alertaRepository.findByAlertaLeidaFalse();

        return alertas.stream()
                .map(AlertaSimpleDTO:: fromEntity)
                .collect(Collectors.toList());
    }




    @Override
    public AlertaResponseDTO actualizar(Long id, AlertaUpdateDTO dto){
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada por ID: " +id));

        // Actualiza relaciones
        alerta.setAlertaLeida(dto.getLeida());
        //Guarda alerta actualizada
        Alerta guardar = alertaRepository.save(alerta);
        //Convierte entidad a Response
        return AlertaResponseDTO.fromEntity(guardar);

    }

    @Override
    public void cambiarEstado(Long id){
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada por ID: " +id));
        alerta.setAlertaLeida(true);
        alertaRepository.save(alerta);
    }

    @Override
    public void eliminar(Long id){
        if(!alertaRepository.existsById(id)){
            throw new RuntimeException("Alerta no encontrada por ID: " +id);
        }
        alertaRepository.deleteById(id);
    }


    //Métodos auxiliares
    private Producto buscarProducto(Long productoId){
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado por ID: " +productoId));
    }

    private Sucursal buscarSucursal(Long sucursalId){
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " +sucursalId));
    }

    private InventarioLotes buscarInventarioLotes(Long inventarioLotesId){
        return inventarioLotesRepository.findById(inventarioLotesId)
                .orElseThrow(() -> new RuntimeException("Inventario Lotes no encontrad por ID: " + inventarioLotesId));
    }


}
