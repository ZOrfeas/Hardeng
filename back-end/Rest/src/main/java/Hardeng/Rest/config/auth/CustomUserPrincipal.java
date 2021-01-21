package Hardeng.Rest.config.auth;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserPrincipal extends User{
    private static final Logger log = LoggerFactory.getLogger(CustomUserPrincipal.class);

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

    public static String makeUserRoleString(String username, String role) {
        log.info("Made this: " + role + '|' + username);
        return role + '|' + username;
    }

    public String retUserRoleString() {
        log.info("Username is: " + this.getUsername());
        log.info("role is: " + this.role);
        
        return makeUserRoleString(this.getUsername(), this.role);
    }

    public static String roleFromUserRoleString(String userRoleString) {
        return userRoleString.split("\\|",2)[0];
    }
    public static String usernameFromUserRoleString(String userRoleString) {
        return userRoleString.split("\\|",2)[1];
    }
}
