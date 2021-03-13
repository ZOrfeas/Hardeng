package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.SessionServiceImpl.SessionObject;

/** Charging Session controller business logic definitions */
public interface SessionService {

    /** Creates a new ChargingSession in persistent data storage
     * @param startedOn
     * @param finishedOn
     * @param energyDelivered
     * @param payment
     * @param chargingPointId
     * @param pricePolicyId
     * @param carId
     * @param driverId
     * @return DTO with information about ChargingSession created
     * @throws NoDataException
     */
    SessionObject createSession(String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException;

    /** Reads a ChargingSession's info by id
     * @param sessionId
     * @return DTO with information of ChargingSession requested
     * @throws NoDataException
     */
    SessionObject readSession(Integer sessionId) throws NoDataException;

    /** Updates a ChargingSession's info
     * @param sessionId
     * @param startedOn
     * @param finishedOn
     * @param energyDelivered
     * @param payment
     * @param chargingPointId
     * @param pricePolicyId
     * @param carId
     * @param driverId
     * @return DTO with updated information of ChargingSession
     * @throws NoDataException
     */
    SessionObject updateSession(Integer sessionId, String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException;
    
    /** Deletes a ChargingSession's data from persistent storage
     * @param sessionId
     * @return HTTP Response Entity with proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> deleteSession(Integer sessionId) throws NoDataException;

    /** Initiates a ChargingSession on specified ChargingStation by marking one of its points as "occupied"
     * @param stationId
     * @return HTTP Response Entity with ChargingPoint ID in body
     * @throws InternalServerErrorException
     */
    ResponseEntity<Object> initiateSession(Integer stationId) throws InternalServerErrorException;

    /** Marks specified ChargingPoint as "not occupied" 
     * @param pointId
     * @throws InternalServerErrorException
     */
    void releasePoint(Integer pointId) throws InternalServerErrorException;
}