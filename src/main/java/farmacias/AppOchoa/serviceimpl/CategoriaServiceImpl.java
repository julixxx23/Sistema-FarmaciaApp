package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.dto.categoria.CategoriaCreateDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaResponseDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaSimpleDTO;
import farmacias.AppOchoa.dto.categoria.CategoriaUpdateDTO;
import farmacias.AppOchoa.model.Categoria;
import farmacias.AppOchoa.repository.CategoriaRepository;
import farmacias.AppOchoa.services.CategoriaService;
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

    // 1. VALIDAR unicidad del nombre
    @Override
    public CategoriaResponseDTO crear(CategoriaCreateDTO dto){
        if(categoriaRepository.existsByCategoriaNombre(dto.getNombre())){
            throw new RuntimeException("Ya existe una categoria con ese nombre: " + dto.getNombre());
        }

        // 2. CONVERTIR DTO a Entidad
        Categoria categoria = Categoria.builder()
                .categoriaNombre(dto.getNombre())
                .categoriaEstado(true)
                .build();

        // 3. GUARDAR en base de datos
        Categoria guardada = categoriaRepository.save(categoria);

        // 4. CONVERTIR Entidad a ResponseDTO
        return CategoriaResponseDTO.fromEntity(guardada);
    }

    @Override
    public CategoriaResponseDTO obtenerPorId(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada por id: " + id));

        return CategoriaResponseDTO.fromEntity(categoria);

    }
    @Override
    public List<CategoriaSimpleDTO> listarTodas(){
        //BUSCAR en base de datos
        List<Categoria> categorias = categoriaRepository.findAll();

        return categorias.stream()
                .map(CategoriaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public List<CategoriaSimpleDTO> listarActivas(){
        //CONVERTIR cada entidad a SimpleDTO
        List<Categoria> categoriaActivas = categoriaRepository.findByCategoriaEstadoTrue();

        return categoriaActivas.stream()
                .map(CategoriaSimpleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaUpdateDTO dto){
        //BUSCAR categoría existente
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada con ID: " +id));

        if(!categoria.getCategoriaNombre().equals(dto.getNombre())){
            if(categoriaRepository.existsByCategoriaNombre(dto.getNombre())){
                throw new RuntimeException("Ya existe otra categoria con el nombre: " + dto.getNombre());
            }
        }

        //Actualizar campos
        categoria.setCategoriaNombre(dto.getNombre());
        categoria.setCategoriaEstado(dto.getEstado());

        //Guardar cambios
        Categoria actualizada = categoriaRepository.save(categoria);

        //Convertir a ResponseDTO
        return CategoriaResponseDTO.fromEntity(actualizada);
    }

    @Override
    public  void cambiarEstado(Long id, Boolean estado){
        //Busca categoria
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " +id));

        //Actualiza estado
        categoria.setCategoriaEstado(estado);

        //Guarda estado
        categoriaRepository.save(categoria);

    }

    @Override
    public void eliminar(Long id){
        cambiarEstado(id, false);
    }



    }
