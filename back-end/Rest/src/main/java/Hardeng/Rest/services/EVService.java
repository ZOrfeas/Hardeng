package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.EVServiceImpl.SessEVObject;
import Hardeng.Rest.services.EVServiceImpl.EVObject;
import Hardeng.Rest.services.EVServiceImpl.DriverCarObject;

/** EV controller business logic definitions */
public interface EVService {
    
    /** Gets information of sessions per electric vehicle within specified timeframe
     * @param driverId
     * @param carId
     * @param dateFrom Timeframe begin date
     * @param dateTo Timeframe end date
     * @return DTO with requested information
     * @throws NoDataException
     */
    SessEVObject sessionsPerEV(Integer driverId, Integer carId,
        String dateFrom, String dateTo) throws NoDataException;

    /** Creates a new EV in peristent data storage
     * @param driverId
     * @param carId
     * @param newCap
     * @return
     * @throws NoDataException
     */
    EVObject createEV(Integer driverId, Integer carId, Double newCap) throws NoDataException;

    /** Reads an EV's info by ID and driver ID
     * @param driverId
     * @param carId
     * @return DTO with information of EV requested
     * @throws NoDataException
     */
    EVObject readEV(Integer driverId, Integer carId) throws NoDataException;

    /** Updates an EV's info
     * @param driverId
     * @param carId
     * @param newCap
     * @return DTO with update information of EV
     * @throws NoDataException
     */
    EVObject updateEV(Integer driverId, Integer carId, Double newCap) throws NoDataException;
    
    /** Deletes an EV's data from persistent data storage
     * @param driverId
     * @param carId
     * @return HTTP Response Entity with the proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> deleteEV(Integer driverId, Integer carId) throws NoDataException;

    /** Gets all of a Driver's EVs
     * @param driverId
     * @return DTO with the requested info
     * @throws NoDataException
     */
    DriverCarObject getAllCarDriver(Integer driverId) throws NoDataException;
}