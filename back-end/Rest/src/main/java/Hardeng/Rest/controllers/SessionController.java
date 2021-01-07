package Hardeng.Rest.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.exceptions.ChargingPointNotFoundException;

@RestController
public class SessionController {
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);
    private static final String endpointUrl = Utilities.BASEURL;
    private final ChargingSessionRepository sessRepository;
    private final ChargingPointRepository pointRepository;

    SessionController(ChargingSessionRepository sessRepository, ChargingPointRepository pointRepository) {
        this.sessRepository = sessRepository;
        this.pointRepository = pointRepository;
    }

    @GetMapping(value = endpointUrl + "/SessionsPerPoint/{pointId}/{dateFrom}/{dateTo}",
     produces = {"application/json", "text/csv"})
    public List<ChargingSession> sessionsPerPoint(@PathVariable Integer pointId,
     @PathVariable String dateFrom, @PathVariable String dateTo) {
        
        log.info("Read request for all ChargingSessions in"+ 
         "{" + dateFrom + ", " + dateTo + "} for ChargingPoint with id {"+
         pointId + "}");
        Timestamp queryDateFrom = Utilities.timestampFromString(dateFrom,
         Utilities.DATE_FORMAT);
        Timestamp queryDateTo = Utilities.timestampFromString(dateTo,
         Utilities.DATE_FORMAT);
        log.info("Fetching ChargingPoint entry...");
        ChargingPoint queryPoint = pointRepository.findById(pointId)
         .orElseThrow(() -> new ChargingPointNotFoundException(pointId));
        log.info("Fetching ChargingSessions...");
        return sessRepository.findByChargingPointIdAndByStartedOnBetween(//note: might need to findBy ChargingPointId
         queryPoint, queryDateFrom, queryDateTo);
    }
}
