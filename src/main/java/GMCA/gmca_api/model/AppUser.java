package GMCA.gmca_api.model;

import GMCA.gmca_api.common.BaseEntity;
import jakarta.persistence.*;
import java.util.*;

@Entity @Table(name = "app_users")
public class AppUser extends BaseEntity {
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String passwordHash;
    @Column(nullable = false) private boolean enabled = true;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) @Column(name = "role", nullable = false)
    private Set<Role> roles = new HashSet<>();
    protected AppUser() {}
    public AppUser(String username, String passwordHash, Set<Role> roles) { this.username = username; this.passwordHash = passwordHash; this.roles = roles; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isEnabled() { return enabled; }
    public Set<Role> getRoles() { return roles; }
}
