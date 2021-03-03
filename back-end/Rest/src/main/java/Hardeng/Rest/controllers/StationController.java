package Hardeng.Rest.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.StationService;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;
import Hardeng.Rest.services.StationServiceImpl.EnergySumObject;
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject;
import Hardeng.Rest.services.StationServiceImpl.StationObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class StationController {
    private static final Logger log = LoggerFactory.getLogger(StationController.class);
    
    @Autowired
    StationService stationService;

    @GetMapping(value = "/SessionsPerStation/{stationId}/{dateFrom}/{dateTo}",
     produces = {"application/json", "text/csv"})
    public SessStationObject sessionsPerStation(@PathVariable(required = false) Integer stationId,
     @PathVariable(required = false) String dateFrom, @PathVariable(required = false) String dateTo) {
        log.info("Sessions per Charging Station requested...");
        if (stationId == null || dateFrom == null || dateTo == null) throw new BadRequestException();
        return stationService.sessionsPerStation(stationId, dateFrom, dateTo);
   }

   @GetMapping(value = "/NearbyStations/{latitude}/{longitude}/{radius}", produces = {"application/json"})
   public List<NearbyStationObject> nearbyStations(@PathVariable(name = "latitude") Double latitude, 
   @PathVariable(name = "longitude") Double longitude, @PathVariable(name = "radius") Double radius) {
      log.info("Nearby stations requested...");
      if (latitude == null || longitude == null || radius == null) throw new BadRequestException();
      return stationService.nearbyStations(latitude, longitude, radius);
   }

   /* CRUD for Charging Station */
   public static class RequestStation {
      private Double lat;
      private Double lon;
      private String address;
      private Integer adminId;
      private Integer providerId;

      public Double getLat() {return this.lat;}
      public Double getLon() {return this.lon;}
      public String getAddress() {return this.address;}
      public Integer getAdminId() {return this.adminId;}
      public Integer getProviderId() {return this.providerId;}

      public void setLat(Double lat) {this.lat = lat;}
      public void setLon(Double lon) {this.lon =lon;}
      public void setAddress(String address) {this.address = address;}
      public void setAdminId(Integer adminId) {this.adminId = adminId;}
      public void setProviderId(Integer providerId) {this.providerId = providerId;}

   }

   @PostMapping(value = "/Station", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public StationObject createStation(@RequestBody RequestStation station) {
      log.info("Create station...");
      if (station.getLat() == null || station.getLon() == null || station.getAddress() == null || 
      station.getAdminId() == null || station.getProviderId() == null) 
         throw new BadRequestException();
      return stationService.createStation(station.getLat(), station.getLon(), 
         station.getAddress(), station.getAdminId(), station.getProviderId());
   }

   @GetMapping(value = "/Station/{stationId}", produces = {"application/json"})
   public StationObject readStation(@PathVariable(name = "stationId") Integer stationId) {
      log.info("Read station...");
      if (stationId == null) throw new BadRequestException();
      return stationService.readStation(stationId);
   }

   @PutMapping(value = "/Station/{stationId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
   public StationObject updateStation(@PathVariable(name = "stationId") Integer stationId, @RequestBody RequestStation station) {
      log.info("Update station...");
      if (stationId == null || station.getLat() == null || station.getLon() == null || station.getAddress() == null || 
      station.getAdminId() == null || station.getProviderId() == null) 
         throw new BadRequestException();
      return stationService.updateStation(stationId, station.getLat(), station.getLon(), 
         station.getAddress(), station.getAdminId(), station.getProviderId());
   }

   @DeleteMapping(value = "/Station/{stationId}")
   public ResponseEntity<Object> deleteStation(@PathVariable(name = "stationId") Integer stationId) {
      log.info("Delete station...");
      if (stationId == null) throw new BadRequestException();
      return stationService.deleteStation(stationId);
   }

   @GetMapping(value = "/AdminStations/{adminId}")
   public List<StationObject> getAdminStations(@PathVariable(name = "adminId") Integer adminId) {
      log.info("Get admin's stations...");
      if (adminId == null) throw new BadRequestException();
      return stationService.getAdminStations(adminId);
   }


   @PostMapping(value = "/AdminAreaStationEnergy/{adminId}",consumes=MediaType.APPLICATION_JSON_VALUE,
   produces = {"application/json"})
   public EnergySumObject getAreaStationSum(@RequestParam(name = "latitude") Double latitude, 
      @RequestParam(name = "longitude") Double longitude, @RequestParam(name = "radius") Double radius, 
      @RequestParam(name = "adminId") Integer adminId, @RequestParam(name = "dateFrom") String dateFrom,
      @RequestParam(name = "dateTo") String dateTo) {
         log.info("Get admin's stations in area and sum of energy transactions...");
         if (latitude == null || longitude == null || radius == null || adminId == null) throw new BadRequestException();
         return stationService.getAreaStationSum(latitude,longitude,radius,adminId,dateFrom,dateTo);
      }
}
