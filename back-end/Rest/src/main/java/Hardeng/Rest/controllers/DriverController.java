package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
import Hardeng.Rest.config.auth.SecurityConfig;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.DriverService;
import Hardeng.Rest.services.DriverServiceImpl.DriverObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DriverController {
    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    
    @Autowired
    DriverService driverService;

   /* CRUD for Driver */
   public static class RequestDriver {
      private String driverName;
      private String username;
      private String password;
      private String email;
      private Integer bonusPoints;
      private Long cardId;
      private Long walletId;

      public String getDriverName() {return this.driverName;}
      public String getUsername() {return this.username;}
      public String getPassword() {return this.password;}
      public String getEmail() {return this.email;}
      public Integer getBonusPoints() {return this.bonusPoints;}
      public Long getCardId() {return this.cardId;}
      public Long getWalletId() {return this.walletId;}
  
      public void setDriverName(String driverName) {this.driverName = driverName;}
      public void setUsername(String username) {this.username = username;}
      public void setPassword(String password) {this.password = password;}
      public void setEmail(String email) {this.email = email;}
      public void setBonusPoints(Integer bonusPoints) {this.bonusPoints = bonusPoints;}
      public void setCardId(Long cardId) {this.cardId = cardId;}
      public void setWalletId(Long walletId) {this.walletId = walletId;}
   }

   @PostMapping(value = "/Driver", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public DriverObject createDriver(@RequestBody RequestDriver driver) {
      log.info("Create driver...");
      if (driver.getDriverName() == null | driver.getUsername() == null || driver.getPassword() == null ||
        driver.getEmail() == null) 
         throw new BadRequestException();
      return driverService.createDriver(driver.getDriverName(), driver.getUsername(), driver.getPassword(), 
        driver.getEmail(), driver.getBonusPoints(), driver.getCardId(), driver.getWalletId());
   }

   @GetMapping(value = "/Driver/{driverId}", produces = {"application/json"})
   public DriverObject readDriver(@PathVariable(name = "driverId") Integer driverId) {
      log.info("Read driver...");
      if (driverId == null) throw new BadRequestException();
      return driverService.readDriver(driverId);
   }

   @PutMapping(value = "/Driver/{driverId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public DriverObject updateDriver(@PathVariable(name = "driverId") Integer driverId, @RequestBody RequestDriver driver) {
      log.info("Update driver...");
      if (driverId == null || driver.getDriverName() == null | driver.getUsername() == null || 
        driver.getPassword() == null || driver.getEmail() == null) 
         throw new BadRequestException();
      return driverService.updateDriver(driverId, driver.getDriverName(), driver.getUsername(), driver.getPassword(), 
         driver.getEmail(), driver.getBonusPoints(), driver.getCardId(), driver.getWalletId());
   }

   @DeleteMapping(value = "/Driver/{driverId}")
   public ResponseEntity<Object> deleteDriver(@PathVariable(name = "driverId") Integer driverId) {
      log.info("Delete driver...");
      if (driverId == null) throw new BadRequestException();
      return driverService.deleteDriver(driverId);
   }
   
   @GetMapping(value = "/Driver/getId", produces = {"application/json"})
   public ResponseEntity<Object> getId(@AuthenticationPrincipal CustomUserPrincipal loggedInUser) {
      log.info("Logged in driver's Id requested...");
      if (loggedInUser == null || !loggedInUser.getRole().equals(SecurityConfig.driverRole))
         throw new BadRequestException();
      return driverService.fetchId(loggedInUser.getUsername());
     
   }
}
