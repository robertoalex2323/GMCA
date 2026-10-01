package GMCA.gmca_api.service;

import GMCA.gmca_api.model.Sale;
import GMCA.gmca_api.repository.ClientRepository;
import GMCA.gmca_api.repository.ProductRepository;
import GMCA.gmca_api.repository.SaleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SaleService {
    private final SaleRepository sales;
    private final ClientRepository clients;
    private final ProductRepository products;
    private final AvailabilityService availability;
    private final AuditService audit;

    public SaleService(SaleRepository sales, ClientRepository clients, ProductRepository products, AvailabilityService availability, AuditService audit) {
        this.sales = sales;
        this.clients = clients;
        this.products = products;
        this.availability = availability;
        this.audit = audit;
    }

    @Transactional
    public Sale confirm(ConfirmSaleRequest request) {
        var client = clients.findById(request.clientId()).orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));
        var product = products.findById(request.productId()).orElseThrow(() -> new IllegalArgumentException("Producto no existe"));
        var available = availability.availability(product.getId()).available();
        if (available.compareTo(request.quantity()) < 0) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + available);
        }
        if (request.unitPrice().compareTo(product.getBasePrice()) < 0 && !request.priceExceptionAuthorized()) {
            throw new IllegalStateException("Precio por debajo de base requiere autorizacion de gerencia");
        }
        var total = request.unitPrice().multiply(request.quantity());
        if (client.getCurrentDebt().add(total).compareTo(client.getCreditLimit()) > 0 && !request.creditExceptionAuthorized()) {
            throw new IllegalStateException("Credito excedido requiere autorizacion de gerencia");
        }
        var sale = sales.save(new Sale(client, product, request.quantity(), request.unitPrice(), request.priceExceptionAuthorized(), request.creditExceptionAuthorized()));
        client.increaseDebt(total);
        audit.record("CONFIRM_SALE", "Sale", sale.getId(), "total=" + total);
        return sale;
    }

    public record ConfirmSaleRequest(
            @NotNull UUID clientId,
            @NotNull UUID productId,
            @NotNull @Positive BigDecimal quantity,
            @NotNull @Positive BigDecimal unitPrice,
            boolean priceExceptionAuthorized,
            boolean creditExceptionAuthorized) {}
}