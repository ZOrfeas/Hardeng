package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.EVServiceImpl.SessEVObject;
import Hardeng.Rest.services.EVServiceImpl.EVObject;
import Hardeng.Rest.services.EVServiceImpl.DriverCarObject;

public interface EVService {
    
    SessEVObject sessionsPerEV(Integer driverId, Integer carId,
        String dateFrom, String dateTo) throws NoDataException;

    EVObject createEV(Integer driverId, Integer carId, Double newCap) throws NoDataException;

    EVObject readEV(Integer driverId, Integer carId) throws NoDataException;

    EVObject updateEV(Integer driverId, Integer carId, Double newCap) throws NoDataException;
    
    ResponseEntity<Object> deleteEV(Integer driverId, Integer carId) throws NoDataException;

    DriverCarObject getAllCarDriver(Integer driverId) throws NoDataException;
}