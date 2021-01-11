package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.PointService;
import Hardeng.Rest.services.PointServiceImpl.SessPointObject;

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

}
