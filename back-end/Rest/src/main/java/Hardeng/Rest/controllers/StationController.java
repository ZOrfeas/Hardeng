package Hardeng.Rest.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.StationService;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject;

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
}
