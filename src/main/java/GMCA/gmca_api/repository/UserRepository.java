package GMCA.gmca_api.repository;


import GMCA.gmca_api.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, UUID> { Optional<AppUser> findByUsername(String username); }
