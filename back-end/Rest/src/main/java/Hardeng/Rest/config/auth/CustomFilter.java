package Hardeng.Rest.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomFilter extends OncePerRequestFilter {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServ;
    @Autowired
    private TokenUtil tokenUtil;
    
}
