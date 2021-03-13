package Hardeng.Rest.config.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/** Custom Implementation of userdetails.User to be placed in current context's AuthenticationPrincipal after authentication */
public class CustomUserPrincipal extends User{

    private static final long serialVersionUID = 6520696521415822840L;

    private final String role;
    
    /**
     * Constructor wrapper, calls extending class' constructor first
     * @param username Authenticated user's username
     * @param role Authenticated user's role
     * @param password Authenticated user's encrypted password
     * @param authorities Authenticated user's applied authorities
     */
    public CustomUserPrincipal(String username, String role,
     String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.role = role;

    }
    /**
     * this.role getter
     * @return role of current instance
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Utility function combining username and role 
     * @param username username string
     * @param role role string
     * @return "role|username"
     */
    public static String makeUserRoleString(String username, String role) {
        return role + '|' + username;
    }

    /** Wraper of {@link #makeUserRoleString(String, String)} for current instance
     * @return this object's combined string
     */
    public String retUserRoleString() {
        return makeUserRoleString(this.getUsername(), this.role);
    }

    /** Utility extracting role from combined string
     * @param userRoleString combined string 
     * @return role
     */
    public static String roleFromUserRoleString(String userRoleString) {
        return userRoleString.split("\\|",2)[0];
    }
    /** Utility extracting username from combined string
     * @param userRoleString combined string
     * @return username
     */
    public static String usernameFromUserRoleString(String userRoleString) {
        return userRoleString.split("\\|",2)[1];
    }
}
