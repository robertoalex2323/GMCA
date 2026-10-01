package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    @Query("select coalesce(sum(s.quantity), 0) from Sale s where s.product.id = :productId and s.status = GMCA.gmca_api.model.SaleStatus.CONFIRMED")
    BigDecimal committed(@Param("productId") UUID productId);
}
