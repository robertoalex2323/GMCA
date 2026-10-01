package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;

@Entity @Table(name = "audit_logs")
public class AuditLog extends BaseEntity {
    @Column(nullable = false) private String actor;
    @Column(nullable = false) private String action;
    @Column(nullable = false) private String entityName;
    @Column(nullable = false) private String entityId;
    @Column(columnDefinition = "text") private String detail;
    protected AuditLog() {}
    public AuditLog(String actor, String action, String entityName, String entityId, String detail) { this.actor = actor; this.action = action; this.entityName = entityName; this.entityId = entityId; this.detail = detail; }
}
