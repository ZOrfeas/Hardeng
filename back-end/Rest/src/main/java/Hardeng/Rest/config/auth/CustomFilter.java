package Hardeng.Rest.config.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import Hardeng.Rest.Utilities.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class CustomFilter extends OncePerRequestFilter {
	private static Logger log = LoggerFactory.getLogger(CustomFilter.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsServ;
    @Autowired
    private TokenUtil tokenUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) 
                                    throws ServletException, IOException
    {
        final String requestTokenHeader = request.getHeader(SecurityConstants.header_name);
        String username = null;
        String token = null;
        if (requestTokenHeader != null) {
            token = requestTokenHeader;
            try {
                username = tokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.info("Token failed to give username");
            } catch (ExpiredJwtException e) {
                log.info("Token has expired");
            }
        } else {
            log.warn("Token was null");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserPrincipal cUserDetails = (CustomUserPrincipal) userDetailsServ.loadUserByUsername(username);

            if (tokenUtil.validateToken(token, cUserDetails)) {
                UsernamePasswordAuthenticationToken upaToken =
                    new UsernamePasswordAuthenticationToken(cUserDetails, null, cUserDetails.getAuthorities());
                upaToken.
                    setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upaToken);
            }
        } else {
            log.warn("Username was null OR was already authenticated");
        }
        
        chain.doFilter(request, response);
    }
}
