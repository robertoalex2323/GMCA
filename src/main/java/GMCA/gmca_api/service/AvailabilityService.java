package GMCA.gmca_api.service;

import GMCA.gmca_api.model.ProductType;
import GMCA.gmca_api.repository.ProductRepository;
import GMCA.gmca_api.repository.ProductionPlanRepository;
import GMCA.gmca_api.repository.SaleRepository;
import GMCA.gmca_api.repository.StockPushRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {
    private final ProductRepository products;
    private final StockPushRepository stockPushes;
    private final ProductionPlanRepository productionPlans;
    private final SaleRepository sales;

    public AvailabilityService(ProductRepository products, StockPushRepository stockPushes, ProductionPlanRepository productionPlans, SaleRepository sales) {
        this.products = products;
        this.stockPushes = stockPushes;
        this.productionPlans = productionPlans;
        this.sales = sales;
    }

    public AvailabilityResponse availability(UUID productId) {
        var product = products.findById(productId).orElseThrow(() -> new IllegalArgumentException("Producto no existe"));
        var stock = stockPushes.physicalStock(productId);
        var programmed = product.getType() == ProductType.FINISHED_RICE ? productionPlans.programmed(productId) : BigDecimal.ZERO;
        var committed = product.getType() == ProductType.FINISHED_RICE ? sales.committed(productId) : BigDecimal.ZERO;
        var available = product.getType() == ProductType.FINISHED_RICE ? stock.add(programmed).subtract(committed) : stock;
        return new AvailabilityResponse(product.getId(), product.getSku(), product.getName(), product.getType(), stock, programmed, committed, available);
    }

    public record AvailabilityResponse(UUID productId, String sku, String name, ProductType type, BigDecimal stockPush, BigDecimal programmedProduction, BigDecimal committed, BigDecimal available) {}
}