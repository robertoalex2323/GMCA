package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, UUID> {
    @Query("select coalesce(sum(p.quantity), 0) from ProductionPlan p where p.product.id = :productId")
    BigDecimal programmed(@Param("productId") UUID productId);
}
