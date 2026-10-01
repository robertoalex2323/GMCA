package GMCA.gmca_api.service;


import GMCA.gmca_api.model.*;
import GMCA.gmca_api.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository logs;
    public AuditService(AuditLogRepository logs) { this.logs = logs; }
    public void record(String action, String entityName, Object entityId, String detail) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var actor = auth == null ? "system" : String.valueOf(auth.getPrincipal());
        logs.save(new AuditLog(actor, action, entityName, String.valueOf(entityId), detail));
    }
}
