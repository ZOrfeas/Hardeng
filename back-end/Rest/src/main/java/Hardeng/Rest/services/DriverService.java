package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.DriverServiceImpl.DriverObject;

public interface DriverService {

    DriverObject createDriver(String driverName, String username, String password, String email,
    Integer bonusPoints, Long cardId, Long walletId) throws NoDataException;

    DriverObject readDriver(Integer driverId) throws NoDataException;

    DriverObject updateDriver(Integer driverId, String driverName, String username, String password,
    String email, Integer bonusPoints, Long cardId, Long walletId) throws NoDataException;
    
    ResponseEntity<Object> deleteDriver(Integer driverId) throws NoDataException;

    ResponseEntity<Object> fetchId(String username) throws BadRequestException;
}