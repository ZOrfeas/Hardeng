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
                if (SecurityConstants.masterUsername.equals(realUsername)) {
                    toRet = new CustomUserPrincipal(realUsername, role, encoder.encode(SecurityConstants.masterPassword), SecurityConfig.masterAuthorities);
                } else {
                    throw new UsernameNotFoundException("Master admin username was not correct");
                }
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
    /**
     * {@link Hardeng.Rest.models.Driver#Driver(String, String, String, String, Integer, Long, Long) Driver}
     * @return created Driver Object with encrypted password
     */
    public Driver makeDriver(String driverName, String username, String password, String email, Integer bonusPoints, Long cardID, Long walletID) {
        Driver toRet = new Driver(driverName, username, password, email, bonusPoints, cardID, walletID);
        toRet.setPassword(encoder.encode(toRet.getPassword()));
        return toRet;
    }
    public Driver makeDriver(String driverName, String username, String password, String email) {
        return makeDriver(driverName, username, password, email, 0, null, null);
    }

    /**
     * {@link Hardeng.Rest.models#Admin(String, String, String, String, String, String, String) Admin}
     * @return created Admin Object with encrypted password
     */
    public Admin makeAdmin(String username, String password, String email, String companyName, String companyPhone, String companyLocation) {
        Admin toRet = new Admin(username, password, email, companyName, companyPhone, companyLocation);
        toRet.setPassword(encoder.encode(toRet.getPassword()));
        return toRet;
    }
    public Admin makeAdmin(String username, String password) {
        return makeAdmin(username, password, null, "undefined", null, null);
    }

    /**
     * Encrypts a Driver's password
     */
    public Driver encryptDriver(Driver driver) {
        driver.setPassword(encoder.encode(driver.getPassword()));
        return driver;
    }
    /**
     * Encrypts an admin's password
     */
    public Admin encryptAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return admin;
    }

}
