package GMCA.gmca_api.controller;


import GMCA.gmca_api.model.*;
import GMCA.gmca_api.repository.*;
import GMCA.gmca_api.service.*;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
    @GetMapping("/api/health") Map<String, String> health() { return Map.of("status", "ok", "app", "gmca-api"); }
}
