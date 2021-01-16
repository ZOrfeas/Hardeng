package Hardeng.Rest.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.Utilities.SecurityConstants;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService {
    
    @Autowired
    private DriverRepository driverRepo;
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        String role = CustomUserPrincipal.roleFromUserRoleString(username);
        String realUsername = CustomUserPrincipal.usernameFromUserRoleString(username);
        CustomUserPrincipal toRet;

        switch (role) {
            case (SecurityConfig.masterAdminRole):
                if (SecurityConstants.masterUsername.equals(realUsername)) 
                    toRet = new CustomUserPrincipal(realUsername, role, SecurityConstants.masterPassword, SecurityConfig.masterAuthorities);
                else
                    throw new UsernameNotFoundException("Master admin username was not correct");
                break;
            
            case (SecurityConfig.stationAdminRole):
                Admin admin = adminRepo.findByUsername(realUsername).orElseThrow(
                    () -> new UsernameNotFoundException("Admin with name { " + realUsername + " } not found."));
                toRet = new CustomUserPrincipal(realUsername, role, admin.getPassword(), SecurityConfig.stationAdminAuthorities);

                break;
            
            case (SecurityConfig.driverRole):
                Driver driver = driverRepo.findByUsername(realUsername).orElseThrow(
                    () -> new UsernameNotFoundException("Driver with name { " + realUsername + " } not found."));
                toRet = new CustomUserPrincipal(realUsername, role, driver.getPassword(), SecurityConfig.driverAuthorities);
                break;

            default:
                throw new UsernameNotFoundException("Requested user role did not match any");
        }

        return toRet;
    }
    
    // these should be somehow interwoven with db logic more cleanly/automatically
    public Driver encodeDriver(Driver driver) {
        driver.setPassword(encoder.encode(driver.getPassword()));
        return driver;
    }

    public Admin encodeAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return admin;
    }
}
