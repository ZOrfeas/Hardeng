package Hardeng.Rest.services;

import java.util.List;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;
import Hardeng.Rest.services.StationServiceImpl.EnergySumObject;
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject;
import Hardeng.Rest.services.StationServiceImpl.StationObject;

public interface StationService {
    
    SessStationObject sessionsPerStation(Integer stationId,
        String dateFrom, String dateTo) throws NoDataException;

    List<NearbyStationObject> nearbyStations(Double latitude, 
        Double longitude, Double rad) throws NoDataException;
    
    StationObject createStation(Double lat, Double lon, String address,
        Integer adminId, Integer eProviderId) throws NoDataException;

    StationObject readStation(Integer stationId) throws NoDataException;

    StationObject updateStation(Integer stationId, Double lat, Double lon,
        String address, Integer adminId, Integer eProviderId) throws NoDataException;
    
    ResponseEntity<Object> deleteStation(Integer stationId) throws NoDataException;

    List<StationObject> getAdminStations(Integer adminId) throws NoDataException;

    EnergySumObject getAreaStationSum(Double latitude, Double longitude, Double radius, Integer adminId,
        String dateFrom, String dateTo) throws NoDataException ;
}