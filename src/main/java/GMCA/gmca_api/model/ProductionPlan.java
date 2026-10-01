package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "production_plans")
public class ProductionPlan extends BaseEntity {
    @ManyToOne(optional = false) private Product product;
    @Column(nullable = false) private LocalDate productionDate;
    @Column(nullable = false, precision = 14, scale = 3) private BigDecimal quantity;
    @Column(nullable = false) private boolean approvedExtra;
    protected ProductionPlan() {}
    public ProductionPlan(Product product, LocalDate productionDate, BigDecimal quantity, boolean approvedExtra) { this.product = product; this.productionDate = productionDate; this.quantity = quantity; this.approvedExtra = approvedExtra; }
    public Product getProduct() { return product; }
    public LocalDate getProductionDate() { return productionDate; }
    public BigDecimal getQuantity() { return quantity; }
    public boolean isApprovedExtra() { return approvedExtra; }
}
