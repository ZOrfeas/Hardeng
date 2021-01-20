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

import Hardeng.Rest.Utilities.SecurityConstants;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
import Hardeng.Rest.config.auth.SecurityConfig;
import Hardeng.Rest.config.auth.TokenUtil;
import Hardeng.Rest.exceptions.AdminNotFoundException;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.NotAuthorizedException;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.repositories.AdminRepository;

import Hardeng.Rest.config.auth.UserDetailsServiceImpl;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private DriverRepository cDriverRepo;
    @Autowired
    private AdminRepository cAdminRepo;
    
    @Autowired
    private UserDetailsServiceImpl uDService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private TokenUtil tokenUtil;

    public static class LoginObject {
        @JsonProperty("token")
        private String token;

        LoginObject(String token){
            this.token = token;
        }
    }
    
    private void authenticate(String roleUserString, String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(roleUserString, password));
        } catch (DisabledException e) {
            throw new InternalServerErrorException();
        } catch (BadCredentialsException e) {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public LoginObject login (String username, String password, String type) throws NoDataException {
        log.info("Beginning login attemp.");
        LoginObject toRet;
        String roleUserString = CustomUserPrincipal.makeUserRoleString(username, type);
        // remove this
        log.info(roleUserString+' '+password);
        // remove this
        authenticate(roleUserString, password);
        CustomUserPrincipal tempUser = (CustomUserPrincipal) uDService.loadUserByUsername(roleUserString);
        toRet = new LoginObject(tokenUtil.generateToken(tempUser));
        return toRet;
        // switch (type) {
        //     case (SecurityConfig.masterAdminRole):
                
        //     break;

        //     case (SecurityConfig.stationAdminRole):

        //     break;

        //     case (SecurityConfig.driverRole):

        //     break;

        //     default:
        //     throw new BadRequestException();
        // }
    }
}