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

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.PointService;
import Hardeng.Rest.services.PointServiceImpl.SessPointObject;
import Hardeng.Rest.services.PointServiceImpl.PointObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PointController {
    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    
    @Autowired
    PointService pointService;

    @GetMapping(value = "/SessionsPerPoint/{pointId}/{dateFrom}/{dateTo}",
     produces = {"application/json", "text/csv"})
    public SessPointObject sessionsPerPoint(@PathVariable(required = false) Integer pointId,
     @PathVariable(required = false) String dateFrom, @PathVariable(required = false) String dateTo) {
        log.info("Sessions per Charging Point requested...");
        if (pointId == null || dateFrom == null || dateTo == null) throw new BadRequestException();
        return pointService.sessionsPerPoint(pointId, dateFrom, dateTo);
   }

   /* CRUD for Charging Point */
   public static class RequestPoint {
      private Integer currentCondition;
      private Integer maxEnergyOutput;
      private Boolean isOccupied;
      private Integer chargerType;
      private Integer stationId;

      public Integer getCurrentCondition() {return this.currentCondition;}
      public Integer getMaxEnergyOutput() {return this.maxEnergyOutput;}
      public Boolean getIsOccupied() {return this.isOccupied;}
      public Integer getChargerType() {return this.chargerType;}
      public Integer getStationId() {return this.stationId;}
  
      public void setCurrentCondition(Integer newCondition) {this.currentCondition = newCondition;}
      public void setMaxOutput(Integer newMaxOutput) {this.maxEnergyOutput = newMaxOutput;}
      public void setIsOccupied(Boolean isOccupied) {this.isOccupied = isOccupied;}
      public void setChargerType(Integer chargerType) {this.chargerType = chargerType;}
      public void setStationId(Integer stationId) {this.stationId = stationId;}
   }

   @PostMapping(value = "/Point", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public PointObject createPoint(@RequestBody RequestPoint point) {
      log.info("Create point...");
      if (point.getCurrentCondition() == null || point.getMaxEnergyOutput() == null || point.getIsOccupied() == null || 
      point.getChargerType() == null || point.getStationId() == null) 
         throw new BadRequestException();
      return pointService.createPoint(point.getCurrentCondition(), point.getMaxEnergyOutput(), 
         point.getIsOccupied(), point.getChargerType(), point.getStationId());
   }

   @GetMapping(value = "/Point/{pointId}", produces = {"application/json"})
   public PointObject readPoint(@PathVariable(name = "pointId") Integer pointId) {
      log.info("Read point...");
      if (pointId == null) throw new BadRequestException();
      return pointService.readPoint(pointId);
   }

   @PutMapping(value = "/Point/{pointId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public PointObject updatePoint(@PathVariable(name = "pointId") Integer pointId, @RequestBody RequestPoint point) {
      log.info("Update point...");
      if (pointId == null || point.getCurrentCondition() == null || point.getMaxEnergyOutput() == null ||
      point.getIsOccupied() == null || point.getChargerType() == null || point.getStationId() == null) 
         throw new BadRequestException();
      return pointService.updatePoint(pointId, point.getCurrentCondition(), point.getMaxEnergyOutput(), 
         point.getIsOccupied(), point.getChargerType(), point.getStationId());
   }

   @DeleteMapping(value = "/Point/{pointId}")
   public ResponseEntity<Object> deletePoint(@PathVariable(name = "pointId") Integer pointId) {
      log.info("Delete point...");
      if (pointId == null) throw new BadRequestException();
      return pointService.deletePoint(pointId);
   }

}
