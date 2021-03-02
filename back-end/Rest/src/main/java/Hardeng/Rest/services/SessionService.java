package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.SessionServiceImpl.SessionObject;

public interface SessionService {

    SessionObject createSession(String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException;

    
    SessionObject readSession(Integer sessionId) throws NoDataException;


    SessionObject updateSession(Integer sessionId, String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException;
    
    ResponseEntity<Object> deleteSession(Integer sessionId) throws NoDataException;

    ResponseEntity<Object> initiateSession(Integer stationId) throws InternalServerErrorException;

    void releasePoint(Integer pointId) throws InternalServerErrorException;
}