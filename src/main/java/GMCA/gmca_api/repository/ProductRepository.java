package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {}
