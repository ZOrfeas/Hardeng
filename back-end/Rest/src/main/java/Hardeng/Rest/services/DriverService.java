package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.DriverServiceImpl.DriverObject;

/** Driver controller business logic definitions */
public interface DriverService {

    /** Creates a new Drvier in persistent data storage
     * @param driverName
     * @param username
     * @param password
     * @param email
     * @param bonusPoints
     * @param cardId
     * @param walletId
     * @return DTO with information about Driver created
     * @throws NoDataException
     */
    DriverObject createDriver(String driverName, String username, String password, String email,
    Integer bonusPoints, Long cardId, Long walletId) throws NoDataException;

    /** Reads a Driver's info by ID
     * @param driverId
     * @return DTO with information of Driver requested
     * @throws NoDataException
     */
    DriverObject readDriver(Integer driverId) throws NoDataException;

    /** Updates a Driver's info
     * @param driverId
     * @param driverName
     * @param username
     * @param password
     * @param email
     * @param bonusPoints
     * @param cardId
     * @param walletId
     * @return DTO with updated information of Driver
     * @throws NoDataException
     */
    DriverObject updateDriver(Integer driverId, String driverName, String username, String password,
    String email, Integer bonusPoints, Long cardId, Long walletId) throws NoDataException;
    
    /** Deletes a Driver's data from persistent storage
     * @param driverId
     * @return HTTP Response Entity with proper code
     * @throws NoDataException
     */
    ResponseEntity<Object> deleteDriver(Integer driverId) throws NoDataException;

    /** Gets a Driver's ID by username
     * @param username
     * @return HTTP Response Entity with the requested ID in body
     * @throws BadRequestException
     */
    ResponseEntity<Object> fetchId(String username) throws BadRequestException;
}