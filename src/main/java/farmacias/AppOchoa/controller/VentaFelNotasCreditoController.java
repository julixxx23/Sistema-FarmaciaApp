package farmacias.AppOchoa.controller;

import farmacias.AppOchoa.services.VentaFelNotasCreditoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ventasnotascreditos")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VentaFelNotasCreditoController {
    private final VentaFelNotasCreditoService ventaFelNotasCreditoService;
}
