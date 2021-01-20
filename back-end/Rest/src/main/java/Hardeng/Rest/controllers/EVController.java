package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.EVService;
import Hardeng.Rest.services.EVServiceImpl.SessEVObject;

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

}
