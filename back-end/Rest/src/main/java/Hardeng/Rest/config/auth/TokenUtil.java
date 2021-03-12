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

/** Grouped JWT manipulation methods */
@Component
public class TokenUtil implements Serializable {

    static final long serialVersionUID = 5471102631213461736L;

    /** Extracts all claims (key-value pairs) from token after applying signing key
     * @return claims
     */
    public Claims getAllClaimsFromToken(String token) {
        Claims temp = Jwts.parser().setSigningKey(SecurityConstants.secret).parseClaimsJws(token).getBody();
        return temp;
    }

    /** Extracts specific claim from token
     * @param <T> type of requested claim
     * @param token token to be queried
     * @param claimsResolver appropriate member method of Claims 
     * @return requested claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /** Extracts username from token
     * @return username in token
     */
    public String getUsernameFromToken(String token) {
        String temp = getClaimFromToken(token, Claims::getSubject);
        return temp;
    }

    /** Extracts Expiration date from token
     * @return expiration date of token
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /** Expiration date tester
     * @return true/false depending on token's expiry date
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /** Token creator method
     * @param claims key-value pairs to be inserted
     * @param subject username of 'owner'
     * @return created token
     */
    private String doGenerateToken(Map<String,Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() +
                        SecurityConstants.validity_time * 1000))
                   .signWith(SignatureAlgorithm.HS512, SecurityConstants.secret)
                   .compact();
    }

    /** Token creator wrapper of {@link #doGenerateToken(Map, String)} for specific UserDetails Object
     * @param userDetails object with current logged in user's info
     * @return created token
     */
    public String generateToken(CustomUserPrincipal userDetails) {
        Map<String,Object> claims = new HashMap<>();
        if (userDetails.getRole().equals(SecurityConfig.masterAdminRole))
            return generateMasterToken();
        else
            return doGenerateToken(claims, userDetails.retUserRoleString());
    }

    /** Utility method creating master token for master user
     * @return created token
     */
    public static String generateMasterToken() {
        return Jwts.builder().setClaims(new HashMap<String,Object>()).
                setSubject(CustomUserPrincipal.makeUserRoleString(SecurityConstants.masterUsername, SecurityConfig.masterAdminRole)).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() +
                    SecurityConstants.a_long_time)).
                signWith(SignatureAlgorithm.HS512, SecurityConstants.secret).
                compact();
    }

    /** Checks if supplied token belongs to supplied user and is valid
     * @param token token in question
     * @param userDetails user in question
     * @return true/false depending on validity
     */
    public Boolean validateToken(String token, CustomUserPrincipal userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.retUserRoleString()) && !isTokenExpired(token));
    }
}
