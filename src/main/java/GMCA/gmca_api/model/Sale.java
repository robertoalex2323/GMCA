package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import GMCA.gmca_api.model.Product;
import GMCA.gmca_api.model.Client;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "sales")
public class Sale extends BaseEntity {
    @ManyToOne(optional = false) private Client client;
    @ManyToOne(optional = false) private Product product;
    @Column(nullable = false, precision = 14, scale = 3) private BigDecimal quantity;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal unitPrice;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SaleStatus status = SaleStatus.CONFIRMED;
    @Column(nullable = false) private boolean priceExceptionAuthorized;
    @Column(nullable = false) private boolean creditExceptionAuthorized;
    protected Sale() {}
    public Sale(Client client, Product product, BigDecimal quantity, BigDecimal unitPrice, boolean priceExceptionAuthorized, boolean creditExceptionAuthorized) { this.client = client; this.product = product; this.quantity = quantity; this.unitPrice = unitPrice; this.priceExceptionAuthorized = priceExceptionAuthorized; this.creditExceptionAuthorized = creditExceptionAuthorized; }
    public Client getClient() { return client; }
    public Product getProduct() { return product; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public SaleStatus getStatus() { return status; }
    public BigDecimal total() { return unitPrice.multiply(quantity); }
}
