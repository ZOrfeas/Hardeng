package Hardeng.Rest.controllers;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.AdminService;
import Hardeng.Rest.services.AdminServiceImpl.SessionStatsObject;
import Hardeng.Rest.services.AdminServiceImpl.StatusObject;
import Hardeng.Rest.services.AdminServiceImpl.UserObject;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
//import java.util.Map;
import Hardeng.Rest.config.auth.SecurityConfig;

//import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


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

    @GetMapping(value = "/users/{username}", produces = {"application/json", "text/csv"})
    public UserObject getUserInfo(@PathVariable(required = false) String username) {
        log.info("Info for user {" + username +"} requested...");
        if (username == null) throw new BadRequestException();
        return adminService.getUserInfo(username);
    }


    @PostMapping(value = "/usermod/{username}/{password}", 
                produces = {"application/json", "text/csv"}, consumes = {"application/json"})
    public StatusObject addOrModifyUser(@RequestParam(name = "driverName") String driverName,
                                        @PathVariable(required = false) String username,
                                        @PathVariable(required = false) String password,
                                        @AuthenticationPrincipal CustomUserPrincipal logedUser,
                                        @RequestParam(name = "email") String email 
                                        ){
        log.info("Usermod requested...");
        if (username == null || password == null) throw new BadRequestException();
        if(logedUser == null) 
        {
            return adminService.userMod(driverName, username, password, "unregistered", email);
        }
        return adminService.userMod(driverName, username, password, logedUser.getRole(), email);
    }

    @PreAuthorize("hasRole('ROLE_" + SecurityConfig.stationAdminRole + "')")
    @PostMapping(value = "/system/sessionupd", produces = {"application/json"},
                 consumes = {"text/csv"})
    public SessionStatsObject sessionUpdate(@RequestParam("file") MultipartFile file) {
        log.info("Session import requested...");
        return adminService.sessionUpdate(file);
    }

}