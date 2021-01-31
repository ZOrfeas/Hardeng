package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.PointServiceImpl.SessPointObject;
import Hardeng.Rest.services.PointServiceImpl.PointObject;

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

    PointObject createPoint(Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException;

    PointObject readPoint(Integer pointId) throws NoDataException;

    PointObject updatePoint(Integer pointId, Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException;
    
    ResponseEntity<Object> deletePoint(Integer pointId) throws NoDataException;
}