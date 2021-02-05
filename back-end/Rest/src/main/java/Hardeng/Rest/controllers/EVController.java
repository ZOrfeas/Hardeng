package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.EVService;
import Hardeng.Rest.services.EVServiceImpl.SessEVObject;
import Hardeng.Rest.services.EVServiceImpl.EVObject;

@RestController
public class EVController {
    private static final Logger log = LoggerFactory.getLogger(EVController.class);
    
    @Autowired
    EVService evService;

    @GetMapping(value = "/SessionsPerEV/{vehicleId}/{dateFrom}/{dateTo}",
     produces = {"application/json", "text/csv"})
    public SessEVObject sessionsPerEV(@PathVariable(required = false) String vehicleId,
     @PathVariable(required = false) String dateFrom, @PathVariable(required = false) String dateTo) {
        log.info("Sessions per EV requested...");
        if (vehicleId == null || dateFrom == null || dateTo == null) throw new BadRequestException();
        String[] tokens = vehicleId.split("-");
        if (tokens.length != 2) throw new BadRequestException();
        Integer driverId = Integer.parseInt(tokens[0]);
        Integer carId = Integer.parseInt(tokens[1]);
        return evService.sessionsPerEV(driverId, carId, dateFrom, dateTo);
   }

   /* CRUD for CarDriver */
   public static class RequestEV {
      private Integer driverId;
      private Integer carId;
      private Double currentCapacity;

      public Integer getDriverId() {return this.driverId;}
      public Integer getCarId() {return this.carId;}
      public Double getCurrentCapacity() {return this.currentCapacity;}
  
      public void setDriverId(Integer driverId) {this.driverId = driverId;}
      public void setCarId(Integer carId) {this.carId = carId;}
      public void setCurrentCapacity(Double currentCapacity) {this.currentCapacity = currentCapacity;}
   }

   @PostMapping(value = "/EV", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public EVObject createEV(@RequestBody RequestEV ev) {
      log.info("Create EV...");
      if (ev.getCarId() == null | ev.getDriverId() == null || ev.getCurrentCapacity() == null) 
         throw new BadRequestException();
      return evService.createEV(ev.getDriverId(), ev.getCarId(), ev.getCurrentCapacity());
   }

   @GetMapping(value = "/EV/{driverId}/{carId}", produces = {"application/json"})
   public EVObject readPoint(@PathVariable(name = "driverId") Integer driverId, 
   @PathVariable(name = "carId") Integer carId) {
      log.info("Read EV...");
      if (driverId == null || carId == null) throw new BadRequestException();
      return evService.readEV(driverId, carId);
   }

   @PutMapping(value = "/EV/{driverId}/{carId}/{newCap}", produces = {"application/json"})
   public EVObject updateEV(@PathVariable(name = "driverId") Integer driverId, 
   @PathVariable(name = "carId") Integer carId, @PathVariable(name = "newCap") Double newCap) {
      log.info("Update EV...");
      if (driverId == null || carId == null || newCap == null) throw new BadRequestException();
      return evService.updateEV(driverId, carId, newCap);
   }

   @DeleteMapping(value = "/EV/{driverId}/{carId}")
   public ResponseEntity<Object> deleteEV(@PathVariable(name = "driverId") Integer driverId, 
   @PathVariable(name = "carId") Integer carId) {
      log.info("Delete EV...");
      if (driverId == null || carId == null) throw new BadRequestException();
      return evService.deleteEV(driverId, carId);
   }

}
