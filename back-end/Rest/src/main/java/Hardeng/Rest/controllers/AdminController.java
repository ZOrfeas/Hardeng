package Hardeng.Rest.controllers;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.models.Admin;

// import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private static final String endpointUrl = Utilities.BASEURL + "/admin";
    private final AdminRepository adRepository;
    private final ChargingSessionRepository sessRepository;

    AdminController(AdminRepository adRepository, ChargingSessionRepository sessRepository) {
        this.adRepository = adRepository;
        this.sessRepository = sessRepository;
    }

    // this can probably not be made to work but would be nice :D
    // @GetMapping(value = endpointUrl + "/healthcheck", produces = Constants.FORMATS)
    @GetMapping(value = endpointUrl + "/healthcheck", produces = {"application/json", "text/csv"})
    public String doHealthCheck() {
        String response = "OK";
        log.info("Beginning healthcheck...");
        // can be probably accomplished with Connection.isValid(), just need to make the Connection part work first
        if(adRepository.doAPing().isEmpty()) {    // most likely needs a try catch statement to catch the possibility of no connection
            response = "failed";                // but could not find what exception to catch. Testing or any exception possibly solutions
        }
        if(("OK").equals(response)) log.info("Healthckeck successful");
        else log.error("Healthcheck failed");
        return "{\"status\":\"" + response +"\"}";
    }

    @GetMapping(value = endpointUrl + "/resetsessions", produces = {"application/json", "text/csv"})
    public String resetSessions() {
        String response = "OK";
        Admin defaultAdmin;
        log.info("Resetting sessions...");
        sessRepository.deleteAll();     // didn't find any way that this can fail, so dunno when to return failed
        log.info("Sessions cleared.");     
        if((defaultAdmin = adRepository.findByUsername("admin")) != null){
            log.info("Default admin already exists. Deleting...");
            adRepository.delete(defaultAdmin);
            log.info("Default admin deleted.");
        }
        defaultAdmin = new Admin("admin", "petrol4ever");
        defaultAdmin = adRepository.save(defaultAdmin);
        log.info("Default admin created.");
        return "{\"status\":\"" + response +"\"}";
    }
}
