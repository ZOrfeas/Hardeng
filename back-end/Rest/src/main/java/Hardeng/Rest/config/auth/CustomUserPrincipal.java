package Hardeng.Rest.config.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserPrincipal extends User{

    private static final long serialVersionUID = 6520696521415822840L;

    private final String role;
    
    public CustomUserPrincipal(String username, String role,
     String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.role = role;

    }
    public String getRole() {
        return this.role;
    }
}
