package GMCA.gmca_api.controller;


import GMCA.gmca_api.model.*;
import GMCA.gmca_api.repository.*;
import GMCA.gmca_api.service.*;
import GMCA.gmca_api.service.AuditService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/inventory")
public class InventoryController {
    private final ProductRepository products; private final StockPushRepository stockPushes; private final ProductionPlanRepository productionPlans; private final AvailabilityService availability; private final AuditService audit;
    public InventoryController(ProductRepository products, StockPushRepository stockPushes, ProductionPlanRepository productionPlans, AvailabilityService availability, AuditService audit) { this.products = products; this.stockPushes = stockPushes; this.productionPlans = productionPlans; this.availability = availability; this.audit = audit; }
    @GetMapping("/products") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_VENTAS','ROLE_ALMACEN','ROLE_PRODUCCION')") List<Product> products() { return products.findAll(); }
    @PostMapping("/products") @PreAuthorize("hasAuthority('ROLE_GERENCIA')") Product createProduct(@Valid @RequestBody ProductRequest r) { var p = products.save(new Product(r.sku(), r.name(), r.type(), r.basePrice())); audit.record("CREATE", "Product", p.getId(), p.getSku()); return p; }
    @GetMapping("/availability/{productId}") @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_VENTAS','ROLE_ALMACEN','ROLE_PRODUCCION')") AvailabilityService.AvailabilityResponse availability(@PathVariable UUID productId) { return availability.availability(productId); }
    @PostMapping("/stock-push") @PreAuthorize("hasAnyAuthority('ROLE_ALMACEN','ROLE_GERENCIA')") StockPush stockPush(@Valid @RequestBody StockPushRequest r) { var p = products.findById(r.productId()).orElseThrow(() -> new IllegalArgumentException("Producto no existe")); var s = stockPushes.save(new StockPush(p, r.stockDate(), r.physicalStock())); audit.record("STOCK_PUSH", "StockPush", s.getId(), p.getSku()); return s; }
    @PostMapping("/production-plans") @PreAuthorize("hasAnyAuthority('ROLE_PRODUCCION','ROLE_GERENCIA')") ProductionPlan production(@Valid @RequestBody ProductionRequest r) { if (r.approvedExtra() && r.quantity().signum() <= 0) throw new IllegalArgumentException("Cantidad invalida"); var p = products.findById(r.productId()).orElseThrow(() -> new IllegalArgumentException("Producto no existe")); var plan = productionPlans.save(new ProductionPlan(p, r.productionDate(), r.quantity(), r.approvedExtra())); audit.record("PRODUCTION_PLAN", "ProductionPlan", plan.getId(), p.getSku()); return plan; }
    record ProductRequest(@NotBlank String sku, @NotBlank String name, @NotNull ProductType type, @NotNull @PositiveOrZero BigDecimal basePrice) {}
    record StockPushRequest(@NotNull UUID productId, @NotNull LocalDate stockDate, @NotNull @PositiveOrZero BigDecimal physicalStock) {}
    record ProductionRequest(@NotNull UUID productId, @NotNull LocalDate productionDate, @NotNull @Positive BigDecimal quantity, boolean approvedExtra) {}
}
