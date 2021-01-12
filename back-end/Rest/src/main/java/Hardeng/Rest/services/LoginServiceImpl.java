package Hardeng.Rest.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.AdminNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.repositories.AdminRepository;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private DriverRepository cDriverRepo;
    @Autowired
    private AdminRepository cAdminRepo;
    
    public static class LoginObject {
        @JsonProperty("token")
        private String token;

        LoginObject(String username, String role){
            this.token = "username: " + username + ", role: " + role;
        }
    }
    
    @Override
    public LoginObject login (String username, String password) throws NoDataException {
        log.info("Checking driver entry...");
        Optional<Driver> optDriver = cDriverRepo.findByUsernameAndPassword(username, password);
        
        if (!optDriver.isPresent())
        {
            log.info("Checking admin entry...");
            Optional<Admin> optAdmin = cAdminRepo.findByUsernameAndPassword(username, password);

            if(!optAdmin.isPresent())
            {
                throw new NoDataException();
            }
            else
            {
                log.info("Generating admin token");
                return new LoginObject(optAdmin.get().getUsername(), "admin");
            }
        }
        else
        {
            log.info("Generating admin token");
            return new LoginObject(optDriver.get().getUsername(), "driver");
        }
    }
}