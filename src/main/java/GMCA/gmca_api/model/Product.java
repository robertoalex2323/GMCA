package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "products")
public class Product extends BaseEntity {
    @Column(nullable = false, unique = true) private String sku;
    @Column(nullable = false) private String name;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ProductType type;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal basePrice;
    protected Product() {}
    public Product(String sku, String name, ProductType type, BigDecimal basePrice) { this.sku = sku; this.name = name; this.type = type; this.basePrice = basePrice; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public ProductType getType() { return type; }
    public BigDecimal getBasePrice() { return basePrice; }
}
