package Hardeng.Rest.services;

import Hardeng.Rest.services.StationServiceImpl.SessStationObject;

public interface StationService {
    
    SessStationObject sessionsPerStation(Integer stationId,
        String dateFrom, String dateTo);
}