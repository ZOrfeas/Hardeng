package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.StationService;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;

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

}
