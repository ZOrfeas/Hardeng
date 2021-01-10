package Hardeng.Rest.controllers;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.services.AdminService;
import Hardeng.Rest.services.AdminServiceImpl.StatusObject;

// import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    // this can probably not be made to work but would be nice :D
    // @GetMapping(value = "/healthcheck", produces = Constants.FORMATS)
    @GetMapping(value = "/healthcheck", produces = {"application/json", "text/csv"})
    public StatusObject doHealthCheck() {
        log.info("Healthcheck requested...");
        return adminService.isHealthy();
    }

    @GetMapping(value = "/resetsessions", produces = {"application/json", "text/csv"})
    public StatusObject resetSessions() {
        log.info("Session reset requested...");
        return adminService.resetSessions();
    }
}
