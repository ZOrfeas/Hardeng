package Hardeng.Rest.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import Hardeng.Rest.config.auth.CustomUserPrincipal;
import Hardeng.Rest.config.auth.TokenUtil;
import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.NotAuthorizedException;


import Hardeng.Rest.config.auth.UserDetailsServiceImpl;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserDetailsServiceImpl uDService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private TokenUtil tokenUtil;

    /** DTO for returning JWT after successful login */
    public static class LoginObject {
        @JsonProperty("token")
        private String token;
        public String getToken() {return this.token;}
        LoginObject(String token){
            this.token = token;
        }
    }
    
    private void authenticate(String roleUserString, String password) {
        try {
            log.info(roleUserString);
            log.info(password);
            authManager.authenticate(new UsernamePasswordAuthenticationToken(roleUserString, password));
        } catch (DisabledException e) {
            throw new InternalServerErrorException();
        } catch (BadCredentialsException e) {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public LoginObject login(String username, String password, String type) throws NoDataException {
        log.info("Beginning login attempt...");
        String roleUserString = CustomUserPrincipal.makeUserRoleString(username, type);
        authenticate(roleUserString, password);
        CustomUserPrincipal tempUser = (CustomUserPrincipal) uDService.loadUserByUsername(roleUserString);
        return (new LoginObject(tokenUtil.generateToken(tempUser)));
    }
}