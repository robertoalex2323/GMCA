package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;

@Entity @Table(name = "vehicles")
public class Vehicle extends BaseEntity {
    @Column(nullable = false, unique = true) private String plate;
    private String driverName;
    @Column(nullable = false) private boolean active = true;
    protected Vehicle() {}
    public Vehicle(String plate, String driverName) { this.plate = plate; this.driverName = driverName; }
    public String getPlate() { return plate; }
    public String getDriverName() { return driverName; }
    public boolean isActive() { return active; }
}
