package Hardeng.Rest.services;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.models.Admin;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private ChargingSessionRepository sessionRepo;
    
    /** 
     * Deemed necessary to help with outputing
     * CSV and JSON types of this code. 
     */
    public static class StatusObject {
        @JsonProperty("status")
        private String status;
        StatusObject(String status) {
            this.status = status;
        }
    }
    
    @Override
    public StatusObject isHealthy() {
        log.info("Beginning healthcheck...");
        if (adminRepo.doAPing().isEmpty()) {
            log.error("Healthcheck failed.");
            return new StatusObject("failed");
        } else {
            log.info("Healthcheck successful.");
            return new StatusObject("OK");
        }
    }

    @Override
    public StatusObject resetSessions() {
        Optional<Admin> adminOptional;
        Admin defaultAdmin;
        log.info("Resetting sessions...");
        sessionRepo.deleteAll();
        log.info("Sessions cleared.");
        adminOptional = adminRepo.findByUsername("admin");
        if(adminOptional.isPresent()) {
            log.info("Default admin already exists. Deleting...");
            adminRepo.delete(adminOptional.get());
            log.info("Default admin deleted.");
        }
        defaultAdmin = new Admin("admin", "petrol4ever");
        log.info("Creating default admin...");
        adminRepo.save(defaultAdmin);
        return new StatusObject("OK");
    }
}

