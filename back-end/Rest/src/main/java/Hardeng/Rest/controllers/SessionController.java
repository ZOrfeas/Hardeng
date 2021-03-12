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
import Hardeng.Rest.services.SessionService;
import Hardeng.Rest.services.SessionServiceImpl.SessionObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SessionController {
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    SessionService sessionService;

    /* CRUD for Charging Session */
   public static class RequestSession {
    private String startedOn;
    private String finishedOn;
    private Float energyDelivered;
    private String payment;
    private Integer chargingPointId;
    private Integer pricePolicyId;
    private Integer carId;
    private Integer driverId;

    public String getStartedOn() {return this.startedOn;}
    public String getFinishedOn() {return this.finishedOn;}
    public Float getEnergyDelivered() {return this.energyDelivered;}
    public String getPayment() {return this.payment;}
    public Integer getChargingPointId() {return this.chargingPointId;}
    public Integer getPricePolicyId() {return this.pricePolicyId;}
    public Integer getCarId() {return this.carId;}
    public Integer getDriverId() {return this.driverId;}

    public void setStartedOn(String startedOn) {this.startedOn = startedOn;}
    public void setFinishedOn(String finishedOn) {this.finishedOn = finishedOn;}
    public void setEnergyDelivered(Float energyDelivered) {this.energyDelivered = energyDelivered;}
    public void setPayment(String payment) {this.payment = payment;}
    public void setChargingPointId(Integer chargingPointId) {this.chargingPointId = chargingPointId;}
    public void setPricePolicyId(Integer pricePolicyId) {this.pricePolicyId = pricePolicyId;}
    public void setCarId(Integer carId) {this.carId = carId;}
    public void setDriverId(Integer driverId) {this.driverId = driverId;}

 }

 @PostMapping(value = "/Session", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
 public SessionObject createSession(@RequestBody RequestSession session) {
    log.info("Create session...");
    if (session.getStartedOn() == null || session.getFinishedOn() == null || session.getEnergyDelivered() == null ||
        session.getPayment() == null || session.getChargingPointId() == null || session.getPricePolicyId() == null ||
        session.getCarId() == null || session.getDriverId() == null) 
       throw new BadRequestException();
    return sessionService.createSession(session.getStartedOn(), session.getFinishedOn(), session.getEnergyDelivered(),
        session.getPayment(), session.getChargingPointId(), session.getPricePolicyId(),
        session.getCarId(), session.getDriverId());
 }

 @GetMapping(value = "/Session/{sessionId}", produces = {"application/json"})
 public SessionObject readSession(@PathVariable(name = "sessionId") Integer sessionId) {
    log.info("Read session...");
    if (sessionId == null) throw new BadRequestException();
    return sessionService.readSession(sessionId);
 }


 @PutMapping(value = "/Session/{sessionId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
 public SessionObject updateSession(@PathVariable(name = "sessionId") Integer sessionId, @RequestBody RequestSession session) {
    log.info("Update session...");
    if (session.getStartedOn() == null || session.getFinishedOn() == null || session.getEnergyDelivered() == null ||
        session.getPayment() == null || session.getChargingPointId() == null || session.getPricePolicyId() == null ||
        session.getCarId() == null || session.getDriverId() == null) 
       throw new BadRequestException();
       return sessionService.updateSession(sessionId, session.getStartedOn(), session.getFinishedOn(), session.getEnergyDelivered(),
            session.getPayment(), session.getChargingPointId(), session.getPricePolicyId(),
            session.getCarId(), session.getDriverId());
 }

 @DeleteMapping(value = "/Session/{sessionId}")
 public ResponseEntity<Object> deleteSession(@PathVariable(name = "sessionId") Integer sessionId) {
    log.info("Delete session...");
    if (sessionId == null) throw new BadRequestException();
    return sessionService.deleteSession(sessionId);
 }

 @PostMapping(value = "/InitiateSession/{stationId}")
 public ResponseEntity<Object> initiateSession(@PathVariable(name ="stationId") Integer stationId) {
    log.info("Initiate new session...");
    if (stationId == null) throw new BadRequestException();
    return sessionService.initiateSession(stationId);
 }

 @PostMapping(value = "/FinalizeSession", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
 public SessionObject finalizeSession(@RequestBody RequestSession session) {
   log.info("Create session...");
   if (session.getStartedOn() == null || session.getFinishedOn() == null || session.getEnergyDelivered() == null ||
       session.getPayment() == null || session.getChargingPointId() == null || session.getPricePolicyId() == null ||
       session.getCarId() == null || session.getDriverId() == null) 
      throw new BadRequestException();
   sessionService.releasePoint(session.getChargingPointId());
   return sessionService.createSession(session.getStartedOn(), session.getFinishedOn(), session.getEnergyDelivered(),
   session.getPayment(), session.getChargingPointId(), session.getPricePolicyId(),
   session.getCarId(), session.getDriverId());
 }
}
