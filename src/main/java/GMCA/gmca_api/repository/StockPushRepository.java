package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface StockPushRepository extends JpaRepository<StockPush, UUID> {
    @Query("select coalesce(sum(s.physicalStock), 0) from StockPush s where s.product.id = :productId")
    BigDecimal physicalStock(@Param("productId") UUID productId);
}
