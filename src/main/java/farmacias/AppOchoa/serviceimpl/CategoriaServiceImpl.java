package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaUpdateDTO;
import farmacias.AppOchoa.model.Categoria;
import farmacias.AppOchoa.repository.CategoriaRepository;
import farmacias.AppOchoa.services.CategoriaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaResponseDTO crear(CategoriaCreateDTO dto){
        if(categoriaRepository.existsByCategoriaNombre(dto.getNombre())){
            throw new RuntimeException("Ya existe una categoria con ese nombre: " + dto.getNombre());
        }

        Categoria categoria = Categoria.builder()
                .categoriaNombre(dto.getNombre())
                .categoriaEstado(true)
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada por id: " + id));

        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaSimpleDTO> listarTodas(){
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(CategoriaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaSimpleDTO> listarActivas(){
        List<Categoria> categoriaActivas = categoriaRepository.findByCategoriaEstadoTrue();
        return categoriaActivas.stream()
                .map(CategoriaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // --- NUEVOS MÉTODOS PAGINADOS ---
    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaSimpleDTO> listarTodasPaginadas(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(CategoriaSimpleDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaSimpleDTO> listarActivasPaginadas(Pageable pageable) {
        return categoriaRepository.findByCategoriaEstadoTrue(pageable)
                .map(CategoriaSimpleDTO::fromEntity);
    }

    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaUpdateDTO dto){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada con ID: " +id));

        if(!categoria.getCategoriaNombre().equals(dto.getNombre())){
            if(categoriaRepository.existsByCategoriaNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra categoria con el nombre: " + dto.getNombre());
            }
        }

        categoria.setCategoriaNombre(dto.getNombre());
        categoria.setCategoriaEstado(dto.getEstado());

        Categoria actualizada = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(actualizada);
    }

    @Override
    public void cambiarEstado(Long id, Boolean estado){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + id));

        categoria.setCategoriaEstado(estado);
        categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(Long id){
        // Usamos tu lógica de borrado lógico
        this.cambiarEstado(id, false);
    }
}