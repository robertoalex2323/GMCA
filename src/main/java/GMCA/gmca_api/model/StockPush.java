package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "stock_pushes")
public class StockPush extends BaseEntity {
    @ManyToOne(optional = false) private Product product;
    @Column(nullable = false) private LocalDate stockDate;
    @Column(nullable = false, precision = 14, scale = 3) private BigDecimal physicalStock;
    protected StockPush() {}
    public StockPush(Product product, LocalDate stockDate, BigDecimal physicalStock) { this.product = product; this.stockDate = stockDate; this.physicalStock = physicalStock; }
    public Product getProduct() { return product; }
    public LocalDate getStockDate() { return stockDate; }
    public BigDecimal getPhysicalStock() { return physicalStock; }
}
