package Auth;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

@Named
@SessionScoped
public class KeepRecord implements Serializable {
    private String username;
    private String password;
    private Principal principal;
    private Set<String> roles;
    private String token;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Principal getPrincipal() { return principal; }
    public void setPrincipal(Principal principal) { this.principal = principal; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public void reset() {
        this.username = null;
        this.password = null;
        this.principal = null;
        this.roles = null;
        this.token = null;
    }
}
