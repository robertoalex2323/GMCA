package GMCA.gmca_api.controller;


import GMCA.gmca_api.model.*;
import GMCA.gmca_api.repository.*;
import GMCA.gmca_api.service.*;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/sales")
public class SaleController {
    private final SaleService service; private final SaleRepository sales;
    public SaleController(SaleService service, SaleRepository sales) { this.service = service; this.sales = sales; }
    @PostMapping("/confirm") @PreAuthorize("hasAnyAuthority('ROLE_VENTAS','ROLE_GERENCIA')") Sale confirm(@Valid @RequestBody SaleService.ConfirmSaleRequest request) { return service.confirm(request); }
    @GetMapping @PreAuthorize("hasAnyAuthority('ROLE_GERENCIA','ROLE_VENTAS','ROLE_CONTABILIDAD','ROLE_DESPACHO')") List<Sale> list() { return sales.findAll(); }
}
