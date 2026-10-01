package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {}
