package farmacias.AppOchoa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> manejarResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        Map<String, Object> cuerpoRespuesta = new HashMap<>();
        cuerpoRespuesta.put("timestamp", LocalDateTime.now());
        cuerpoRespuesta.put("mensaje", ex.getMessage());
        cuerpoRespuesta.put("detalles", request.getDescription(false));
        cuerpoRespuesta.put("estado", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(cuerpoRespuesta, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> cuerpoRespuesta = new HashMap<>();
        cuerpoRespuesta.put("timestamp", LocalDateTime.now());
        cuerpoRespuesta.put("mensaje", "Ocurrió un error interno en el servidor");
        cuerpoRespuesta.put("error", ex.getMessage());
        cuerpoRespuesta.put("estado", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(cuerpoRespuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> manejarBadRequestException(BadRequestException ex, WebRequest request) {
        Map<String, Object> cuerpoRespuesta = new HashMap<>();
        cuerpoRespuesta.put("timestamp", LocalDateTime.now());
        cuerpoRespuesta.put("mensaje", ex.getMessage());
        cuerpoRespuesta.put("detalles", request.getDescription(false));
        cuerpoRespuesta.put("estado", HttpStatus.BAD_REQUEST.value()); // 400

        return new ResponseEntity<>(cuerpoRespuesta, HttpStatus.BAD_REQUEST);
    }
}
