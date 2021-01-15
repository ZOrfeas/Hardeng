package Hardeng.Rest.services;

import java.util.List;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject;

public interface StationService {
    
    SessStationObject sessionsPerStation(Integer stationId,
        String dateFrom, String dateTo) throws NoDataException;

        List<NearbyStationObject> nearbyStations(Double latitude, 
        Double longitude, Double rad) throws NoDataException;
}