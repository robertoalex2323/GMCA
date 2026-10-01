package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "clients")
public class Client extends BaseEntity {
    @Column(nullable = false) private String businessName;
    @Column(nullable = false, unique = true) private String documentNumber;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal creditLimit = BigDecimal.ZERO;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal currentDebt = BigDecimal.ZERO;
    @Column(nullable = false) private boolean active = true;
    protected Client() {}
    public Client(String businessName, String documentNumber, BigDecimal creditLimit) { this.businessName = businessName; this.documentNumber = documentNumber; this.creditLimit = creditLimit; }
    public String getBusinessName() { return businessName; }
    public String getDocumentNumber() { return documentNumber; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public BigDecimal getCurrentDebt() { return currentDebt; }
    public boolean isActive() { return active; }
    public void increaseDebt(BigDecimal amount) { currentDebt = currentDebt.add(amount); }
}
