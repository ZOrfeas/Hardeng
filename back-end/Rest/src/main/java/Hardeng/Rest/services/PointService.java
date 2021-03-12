package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.PointServiceImpl.SessPointObject;
import Hardeng.Rest.services.PointServiceImpl.PointObject;

/** Charging Point controller business logic definitions */
public interface PointService {
    
    /**
     * Fetch sessions by charging point and withing dates
     * @param pointId charging pointin question
     * @param dateFrom starting date
     * @param dateTo ending date
     * @return Object with the relevant info
     */
    SessPointObject sessionsPerPoint(Integer pointId,
        String dateFrom, String dateTo) throws NoDataException;

    /** Creates a new ChargingPoint in persistent data storage
     * @param condition
     * @param maxEnergy
     * @param isOccupied
     * @param chargerType
     * @param stationId
     * @return DTO with information about Point created
     * @throws NoDataException
     */
    PointObject createPoint(Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException;

    /** Reads a ChargingPoint's info by id
     * @param pointId
     * @return DTO with information of Point requested
     * @throws NoDataException
     */
    PointObject readPoint(Integer pointId) throws NoDataException;

    /** Updates a ChargingPoint's info
     * @param pointId
     * @param condition
     * @param maxEnergy
     * @param isOccupied
     * @param chargerType
     * @param stationId
     * @return DTO with update information of ChargingPoint
     * @throws NoDataException
     */
    PointObject updatePoint(Integer pointId, Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException;
    
    /** Deletes a ChargingPoint's data from persistent storage
     * @param pointId
     * @return HTTP Response Entity with proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> deletePoint(Integer pointId) throws NoDataException;
}