package Hardeng.Rest.controllers;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.AdminService;
import Hardeng.Rest.services.AdminServiceImpl.SessionStatsObject;
import Hardeng.Rest.services.AdminServiceImpl.StatusObject;
import Hardeng.Rest.services.AdminServiceImpl.UserObject;
import Hardeng.Rest.services.AdminServiceImpl.AdminObject;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
//import java.util.Map;
import Hardeng.Rest.config.auth.SecurityConfig;

//import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
                 consumes = {"multipart/form-data"})
    public SessionStatsObject sessionUpdate(@RequestParam("file") MultipartFile file) {
        log.info("Session import requested...");
        return adminService.sessionUpdate(file);
    }

    /* CRUD for Admin */
   public static class RequestAdmin {
    private String username;
    private String password;
    private String email;
    private String companyName;
    private String companyPhone;
    private String companyLocation;

    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getEmail() {return this.email;}
    public String getCompanyName() {return this.companyName;}
    public String getCompanyPhone() {return this.companyPhone;}
    public String getCompanyLocation() {return this.companyLocation;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    public void setCompanyPhone(String companyPhone) {this.companyPhone = companyPhone;}
    public void setCompanyLocation(String companyLocation) {this.companyLocation = companyLocation;}
 }

 @PostMapping(value = "/Admin", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
 public AdminObject createAdmin(@RequestBody RequestAdmin admin) {
    log.info("Create admin...");
    if (admin.getUsername() == null || admin.getPassword() == null || admin.getEmail() == null ||
        admin.getCompanyName() == null || admin.getCompanyPhone() == null || admin.getCompanyLocation() == null) 
       throw new BadRequestException();
    return adminService.createAdmin(admin.getUsername(), admin.getPassword(), admin.getEmail(),
        admin.getCompanyName(), admin.getCompanyPhone(), admin.getCompanyLocation());
 }

 @GetMapping(value = "/Admin/{adminId}", produces = {"application/json"})
 public AdminObject readAdmin(@PathVariable(name = "adminId") Integer adminId) {
    log.info("Read admin...");
    if (adminId == null) throw new BadRequestException();
    return adminService.readAdmin(adminId);
 }

 @PutMapping(value = "/Admin/{adminId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
 public AdminObject updateAdmin(@PathVariable(name = "adminId") Integer adminId, @RequestBody RequestAdmin admin) {
    log.info("Update admin...");
    if (adminId == null || admin.getUsername() == null || admin.getPassword() == null || admin.getEmail() == null ||
    admin.getCompanyName() == null || admin.getCompanyPhone() == null || admin.getCompanyLocation() == null) 
       throw new BadRequestException();
    return adminService.updateAdmin(adminId, admin.getUsername(), admin.getPassword(), admin.getEmail(),
    admin.getCompanyName(), admin.getCompanyPhone(), admin.getCompanyLocation());
 }

 @DeleteMapping(value = "/Admin/{adminId}")
 public ResponseEntity<Object> deleteAdmin(@PathVariable(name = "adminId") Integer adminId) {
    log.info("Delete admin...");
    if (adminId == null) throw new BadRequestException();
    return adminService.deleteAdmin(adminId);
 }

 @GetMapping(value = "/getId", produces = {"application/json"})
 public ResponseEntity<Object> getId(@AuthenticationPrincipal CustomUserPrincipal loggedInAdmin) {
     log.info("Logged in admin's Id requested...");
     if (loggedInAdmin == null || !loggedInAdmin.getRole().equals(SecurityConfig.stationAdminRole))
        throw new BadRequestException();
    return adminService.fetchId(loggedInAdmin.getUsername());
 }

 @GetMapping(value = "/totalEnergy/{adminId}/{dateFrom}/{dateTo}", produces = {"application/json"})
 public ResponseEntity<Object> getTotalEnergy(@PathVariable(name = "adminId") Integer adminId
    ,@PathVariable(name = "dateFrom") String dateFrom, @PathVariable(name = "dateTo") String dateTo)
    {
        log.info("Admin's total energy consumption requested...");
        if (adminId == null || dateFrom == null || dateTo == null) throw new BadRequestException();
        return adminService.getTotalEnergy(adminId, dateFrom, dateTo);
    }
}