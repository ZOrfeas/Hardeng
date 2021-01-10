package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.services.StationService;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;

@RestController
public class StationController {
    private static final Logger log = LoggerFactory.getLogger(StationController.class);
    
    @Autowired
    StationService stationService;

    @GetMapping(value = "/SessionsPerStation/{stationId}/{dateFrom}/{dateTo}",
     produces = {"application/json", "text/csv"})
    public SessStationObject sessionsPerStationC(@PathVariable Integer stationId,
     @PathVariable String dateFrom, @PathVariable String dateTo) {
        log.info("Sessions per Charging Station requested...");
        return stationService.sessionsPerStation(stationId, dateFrom, dateTo);
   }

}
