package GMCA.gmca_api.controller;


import GMCA.gmca_api.model.*;
import GMCA.gmca_api.repository.*;
import GMCA.gmca_api.service.*;
import GMCA.gmca_api.service.AuditService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/master")
public class MasterController {
    private final ClientRepository clients; private final VehicleRepository vehicles; private final AuditService audit;
    public MasterController(ClientRepository clients, VehicleRepository vehicles, AuditService audit) { this.clients = clients; this.vehicles = vehicles; this.audit = audit; }
    @GetMapping("/clients") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_VENTAS','ROLE_CONTABILIDAD')") List<Client> clients() { return clients.findAll(); }
    @PostMapping("/clients") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_VENTAS')") Client createClient(@Valid @RequestBody ClientRequest r) { var c = clients.save(new Client(r.businessName(), r.documentNumber(), r.creditLimit())); audit.record("CREATE", "Client", c.getId(), c.getDocumentNumber()); return c; }
    @GetMapping("/vehicles") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_DESPACHO','ROLE_ALMACEN')") List<Vehicle> vehicles() { return vehicles.findAll(); }
    @PostMapping("/vehicles") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_DESPACHO')") Vehicle createVehicle(@Valid @RequestBody VehicleRequest r) { var v = vehicles.save(new Vehicle(r.plate(), r.driverName())); audit.record("CREATE", "Vehicle", v.getId(), v.getPlate()); return v; }
    record ClientRequest(@NotBlank String businessName, @NotBlank String documentNumber, @NotNull @PositiveOrZero BigDecimal creditLimit) {}
    record VehicleRequest(@NotBlank String plate, String driverName) {}
}
