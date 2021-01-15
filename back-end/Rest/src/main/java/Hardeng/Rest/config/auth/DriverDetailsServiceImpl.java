package Hardeng.Rest.config.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import Hardeng.Rest.models.Driver;
import Hardeng.Rest.repositories.DriverRepository;

public class DriverDetailsServiceImpl implements  UserDetailsService {
    
    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Driver driver = driverRepo.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("User with name { " + username + " } not found."));

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(SecurityConfig.driverAuthority);
        return new User(driver.getUsername(), driver.getPassword(), grantedAuthorities);  
    }
    // this should be somehow interwoven with db logic more cleanly/automatically
    public Driver save(Driver driver) {
        driver.setPassword(encoder.encode(driver.getPassword()));
        return driverRepo.save(driver);
    }
}
