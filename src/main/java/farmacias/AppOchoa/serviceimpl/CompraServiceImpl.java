package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.compra.CompraCreateDTO;
import farmacias.AppOchoa.dto.compra.CompraResponseDTO;
import farmacias.AppOchoa.dto.compra.CompraSimpleDTO;
import farmacias.AppOchoa.dto.compra.CompraUpdateDTO;
import farmacias.AppOchoa.model.*;
import farmacias.AppOchoa.repository.CompraRepository;
import farmacias.AppOchoa.repository.SucursalRepository;
import farmacias.AppOchoa.repository.UsuarioRepository;
import farmacias.AppOchoa.services.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;

    public CompraServiceImpl(
            CompraRepository compraRepository,
            SucursalRepository sucursalRepository,
            UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.sucursalRepository = sucursalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public CompraResponseDTO crear(CompraCreateDTO dto) {
        Sucursal sucursal = sucursalRepository.findById(dto.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada por ID: " + dto.getSucursalId()));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado por ID: " + dto.getUsuarioId()));

        // Crear Compra Nueva
        Compra compras = Compra.builder()
                .sucursal(sucursal)
                .usuario(usuario)
                .compraFecha(dto.getFechaCompra())
                .compraTotal(dto.getCompraTotal)
                .compraObservaciones(dto.getObservaciones())
                .compraEstado(CompraEstado.ACTIVA)
                .build();

        // Guardar Compra
        Compra guardar = compraRepository.save(compras);

        // Retornar DTO
        return CompraResponseDTO.fromEntity(guardar);
    }

    @Override
    public CompraResponseDTO listarPorId(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada por ID: " + id));

        return CompraResponseDTO.fromEntity(compra);
    }

    @Override
    public List<CompraSimpleDTO> listaTodas() {
        List<Compra> compras = compraRepository.findAll();

        return compras.stream()
                .map(CompraSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompraSimpleDTO> listarActivos() {
        List<Compra> compras = compraRepository.findByCompraEstadoTrue();

        return compras.stream()
                .map(CompraSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CompraResponseDTO actualizar(Long id, CompraUpdateDTO dto) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada por ID: " + id));

        // Solo actualiza observaciones
        if (dto.getObservaciones() != null && !dto.getObservaciones().trim().isEmpty()) {
            compra.setCompraObservaciones(dto.getObservaciones());
        }

        if (dto.getEstado() != null) {
            // Solo permitir cambiar de ACTIVA → ANULADA
            if (compra.getCompraEstado() == CompraEstado.ACTIVA && !dto.getEstado()) {
                // false = anulada
                compra.setCompraEstado(CompraEstado.ANULADA);
            } else if (compra.getCompraEstado() == CompraEstado.ANULADA && dto.getEstado()) {
                // O quizás permitir reactivar si está anulada
                compra.setCompraEstado(CompraEstado.ACTIVA);
            } else {
                throw new RuntimeException("Cambio de estado no permitido");
            }
        }


        Compra compraActualizada = compraRepository.save(compra);
        return CompraResponseDTO.fromEntity(compraActualizada);
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada por ID: " +id));

        CompraEstado nuevoEstado = estado ? CompraEstado.ACTIVA : CompraEstado.ANULADA;
        compra.setCompraEstado(nuevoEstado);

        compraRepository.save(compra);
    }

    @Override
    public  void eliminar(Long id){
        cambiarEstado(id, false);
    }






}

