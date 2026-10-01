package GMCA.gmca_api.controller;

import GMCA.gmca_api.model.DispatchOrder;
import GMCA.gmca_api.model.DispatchStatus;
import GMCA.gmca_api.repository.DispatchOrderRepository;
import GMCA.gmca_api.repository.SaleRepository;
import GMCA.gmca_api.repository.VehicleRepository;
import GMCA.gmca_api.service.AuditService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {
    private final DispatchOrderRepository orders;
    private final SaleRepository sales;
    private final VehicleRepository vehicles;
    private final AuditService audit;

    public DispatchController(DispatchOrderRepository orders, SaleRepository sales, VehicleRepository vehicles, AuditService audit) {
        this.orders = orders;
        this.sales = sales;
        this.vehicles = vehicles;
        this.audit = audit;
    }

    @GetMapping("/board")
    @PreAuthorize("hasAnyAuthority('ROLE_DESPACHO','ROLE_GERENCIA','ROLE_ALMACEN')")
    List<DispatchOrder> board() {
        return orders.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_DESPACHO','ROLE_GERENCIA')")
    DispatchOrder create(@Valid @RequestBody CreateDispatchRequest request) {
        var sale = sales.findById(request.saleId()).orElseThrow(() -> new IllegalArgumentException("Venta no existe"));
        var vehicle = vehicles.findById(request.vehicleId()).orElseThrow(() -> new IllegalArgumentException("Vehiculo no existe"));
        var order = orders.save(new DispatchOrder(sale, vehicle));
        audit.record("CREATE", "DispatchOrder", order.getId(), vehicle.getPlate());
        return order;
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_DESPACHO','ROLE_ALMACEN','ROLE_GERENCIA')")
    @Transactional
    DispatchOrder status(@PathVariable UUID id, @Valid @RequestBody StatusRequest request) {
        var order = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Despacho no existe"));
        order.setStatus(request.status());
        audit.record("DISPATCH_STATUS", "DispatchOrder", order.getId(), request.status().name());
        return order;
    }

    @PostMapping("/{id}/gate-release")
    @PreAuthorize("hasAnyAuthority('ROLE_DESPACHO','ROLE_GERENCIA')")
    @Transactional
    DispatchOrder gateRelease(@PathVariable UUID id) {
        var order = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Despacho no existe"));
        if (order.getStatus() != DispatchStatus.LOADED) {
            order.setStatus(DispatchStatus.BLOCKED);
            audit.record("GATE_BLOCKED", "DispatchOrder", order.getId(), "Carga no finalizada");
            throw new IllegalStateException("Salida bloqueada: la carga no esta 100% finalizada");
        }
        order.setStatus(DispatchStatus.GATE_RELEASED);
        audit.record("GATE_RELEASED", "DispatchOrder", order.getId(), "Salida confirmada");
        return order;
    }

    record CreateDispatchRequest(@NotNull UUID saleId, @NotNull UUID vehicleId) {}
    record StatusRequest(@NotNull DispatchStatus status) {}
}