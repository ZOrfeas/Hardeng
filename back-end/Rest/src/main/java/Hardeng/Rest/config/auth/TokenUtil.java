package Hardeng.Rest.config.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import Hardeng.Rest.Utilities.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil implements Serializable {

    static final long serialVersionUID = 5471102631213461736L;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SecurityConstants.secret).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String,Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() +
                        SecurityConstants.validity_time * 1000))
                   .signWith(SignatureAlgorithm.HS512, SecurityConstants.secret)
                   .compact();
    }

    public String generateToken(CustomUserPrincipal userDetails) {
        Map<String,Object> claims = new HashMap<>();
        if (userDetails.getRole().equals(SecurityConfig.masterAdminRole))
            return generateMasterToken();
        else
            return doGenerateToken(claims, userDetails.retUserRoleString());
    }

    public static String generateMasterToken() {
        return Jwts.builder().setClaims(new HashMap<String,Object>()).
                setSubject(CustomUserPrincipal.makeUserRoleString(SecurityConfig.masterAdminRole, SecurityConstants.masterUsername)).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() +
                    SecurityConstants.a_long_time)).
                signWith(SignatureAlgorithm.HS512, SecurityConstants.secret).
                compact();
    }

    public Boolean validateToken(String token, CustomUserPrincipal userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.retUserRoleString()) && !isTokenExpired(token));
    }
}
