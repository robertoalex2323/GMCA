package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import GMCA.gmca_api.model.Vehicle;
import GMCA.gmca_api.model.Sale;
import jakarta.persistence.*;

@Entity @Table(name = "dispatch_orders")
public class DispatchOrder extends BaseEntity {
    @ManyToOne(optional = false) private Sale sale;
    @ManyToOne(optional = false) private Vehicle vehicle;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private DispatchStatus status = DispatchStatus.PENDING;
    protected DispatchOrder() {}
    public DispatchOrder(Sale sale, Vehicle vehicle) { this.sale = sale; this.vehicle = vehicle; }
    public Sale getSale() { return sale; }
    public Vehicle getVehicle() { return vehicle; }
    public DispatchStatus getStatus() { return status; }
    public void setStatus(DispatchStatus status) { this.status = status; }
}
