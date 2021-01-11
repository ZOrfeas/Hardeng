package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;

public interface StationService {
    
    SessStationObject sessionsPerStation(Integer stationId,
        String dateFrom, String dateTo) throws NoDataException;
}