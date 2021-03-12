package Hardeng.Rest.services;

import java.util.List;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.StationServiceImpl.SessStationObject;
import Hardeng.Rest.services.StationServiceImpl.EnergySumObject;
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject;
import Hardeng.Rest.services.StationServiceImpl.StationObject;

/** Charging Station controller business logic definitions */
public interface StationService {
    
    /** Gets information of sessions per ChargingStation within specified timeframe
     * @param stationId
     * @param dateFrom
     * @param dateTo
     * @return DTO with requested information
     * @throws NoDataException
     */
    SessStationObject sessionsPerStation(Integer stationId,
        String dateFrom, String dateTo) throws NoDataException;

    /** Gets stations within specified area
     * @param latitude
     * @param longitude
     * @param rad radius of area to be queried
     * @return List of DTOs with ChargingStation information
     * @throws NoDataException
     */
    List<NearbyStationObject> nearbyStations(Double latitude, 
        Double longitude, Double rad) throws NoDataException;
    
    /** Creates a new ChargingStation in persistent data storage
     * @param lat
     * @param lon
     * @param address
     * @param adminId
     * @param eProviderId
     * @return DTO with information about ChargingStation created
     * @throws NoDataException
     */
    StationObject createStation(Double lat, Double lon, String address,
        Integer adminId, Integer eProviderId) throws NoDataException;

    /** Reads a ChargingStation's info by id
     * @param stationId
     * @return DTO with information of ChargingStation requested
     * @throws NoDataException
     */
    StationObject readStation(Integer stationId) throws NoDataException;

    /** Updates a ChargingStation's info
     * @param stationId
     * @param lat
     * @param lon
     * @param address
     * @param adminId
     * @param eProviderId
     * @return DTO with updated information of ChargingStation
     * @throws NoDataException
     */
    StationObject updateStation(Integer stationId, Double lat, Double lon,
        String address, Integer adminId, Integer eProviderId) throws NoDataException;
    
    /** Deletes a ChargingStation's data from persistent storage
     * @param stationId
     * @return
     * @throws NoDataException
     */
    ResponseEntity<Object> deleteStation(Integer stationId) throws NoDataException;

    /** Gets ChargingStations "belonging" to an Admin
     * @param adminId
     * @return List of DTOs with ChargingStation information
     * @throws NoDataException
     */
    List<StationObject> getAdminStations(Integer adminId) throws NoDataException;

    /** Gets the total energy consumption of an Admin's station within a specified area
     * @param latitude
     * @param longitude
     * @param radius
     * @param adminId
     * @param dateFrom
     * @param dateTo
     * @return DTO with requested info
     * @throws NoDataException
     */
    EnergySumObject getAreaStationSum(Double latitude, Double longitude, Double radius, Integer adminId,
        String dateFrom, String dateTo) throws NoDataException ;
}