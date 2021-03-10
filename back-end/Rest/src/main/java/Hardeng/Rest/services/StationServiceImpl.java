package Hardeng.Rest.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.Utilities.CsvObject;
import Hardeng.Rest.exceptions.ChargingStationNotFoundException;
import Hardeng.Rest.exceptions.AdminNotFoundException;
import Hardeng.Rest.exceptions.EnergyProviderNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.EnergyProvider;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.EnergyProviderRepository;
import Hardeng.Rest.services.PointServiceImpl;

@Service
public class StationServiceImpl implements StationService {
    private static final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    @Autowired
    private ChargingStationRepository cStationRepo;
    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;
    @Autowired
    private AdminRepository cAdminRepo;
    @Autowired
    private EnergyProviderRepository cEnergyProviderRepo;

    public class PointObject {
        @JsonProperty("PointID")
        @CsvBindByName
        private String pointId;
        @JsonProperty("PointSessions")
        @CsvBindByName
        private Integer pointSessions;
        @JsonProperty("EnergyDelivered")
        @CsvBindByName
        private Float energyDelivered;

        PointObject(Timestamp dateFrom, Timestamp dateTo, ChargingPoint cPoint) {
            this.pointId = cPoint.getId().toString();
            Integer pSess = cSessRepo.countByStartedOnGreaterThanEqualAndFinishedOnLessThanEqualAndChargingPoint(dateFrom, dateTo, cPoint);
            this.pointSessions = pSess == null ? 0 : pSess;
            Float eDeliv = cSessRepo.totalEnergyDelivered(dateFrom, dateTo, cPoint);
            this.energyDelivered =  eDeliv == null ? 0.0f : eDeliv;
        }
    }
    
    public static class CsvSessStationObject {
        @CsvBindByName
        private String stationId;
        @CsvBindByName
        private String operator;
        @CsvBindByName
        private String requestTimeStamp;
        @CsvBindByName
        private String periodFrom;
        @CsvBindByName
        private String periodTo;
        @CsvBindByName
        private Float totalEnergyDelivered;
        @CsvBindByName
        private Integer nrOfChargingSessions;
        @CsvBindByName
        private Integer nrOfActivePoints;
        @CsvBindByName
        private String pointId;
        @CsvBindByName
        private Integer pointSessions;
        @CsvBindByName
        private Float energyDelivered;

        CsvSessStationObject(SessStationObject ss, PointObject po) {
            this.stationId = ss.stationId; this.operator = ss.operator;
            this.requestTimeStamp = ss.requestTimeStamp; this.periodFrom = ss.periodFrom;
            this.periodTo = ss.periodTo; this.totalEnergyDelivered = ss.totalEnergyDelivered;
            this.nrOfChargingSessions = ss.nrOfChargingSessions; this.nrOfActivePoints = ss.nrOfActivePoints;
            this.pointId = po.pointId; this.pointSessions = po.pointSessions; this.energyDelivered = po.energyDelivered;
        }
        public String getStationId() {return this.stationId;}
        public String getOperator() {return this.operator;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Float getTotalEnergyDelivered() {return this.totalEnergyDelivered;}
        public Integer getNrOfChargingSessions() {return this.nrOfChargingSessions;}
        public Integer getNrOfActivePoints() {return this.nrOfActivePoints;}
        public String getPointId() {return this.pointId;}
        public Integer getPointSessions() {return this.pointSessions;}
        public Float getEnergyDelivered() {return this.energyDelivered;}

    }
    
    public class SessStationObject implements CsvObject {
        @JsonProperty("StationID")
        private String stationId;
        @JsonProperty("Operator")
        private String operator;
        @JsonProperty("RequestTimeStamp")
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        private String periodFrom;
        @JsonProperty("PeriodTo")
        private String periodTo;
        @JsonProperty("TotalEnergyDelivered")
        private Float totalEnergyDelivered;
        @JsonProperty("NumberOfChargingSessions")
        private Integer nrOfChargingSessions;
        @JsonProperty("NumberOfActivePoints")
        private Integer nrOfActivePoints;
        @JsonProperty("SessionsSummaryList")
        private List<PointObject> sessionsSummaryList = new ArrayList<>();

        SessStationObject(Timestamp from, Timestamp to,
         ChargingStation cStation, List<ChargingPoint> cPoints){
            this.stationId = cStation.getId().toString();
            this.operator = cStation.getAdmin().getCompanyName();
            this.requestTimeStamp = Utilities.TIMESTAMP_FORMAT.format(new Date());
            this.periodFrom = Utilities.TIMESTAMP_FORMAT.format(from);
            this.periodTo = Utilities.TIMESTAMP_FORMAT.format(to);
            this.nrOfActivePoints = cPoints.size();
            this.totalEnergyDelivered = 0.0f;
            this.nrOfChargingSessions = 0;
            for (int i = 0; i < this.nrOfActivePoints; i++) {
                PointObject pointObject = new PointObject(from, to, cPoints.get(i));
                this.sessionsSummaryList.add(pointObject);
                this.totalEnergyDelivered = this.totalEnergyDelivered + pointObject.energyDelivered;
                this.nrOfChargingSessions = this.nrOfChargingSessions + pointObject.pointSessions;
            }
        }
        /*
        public String getStationId() {return this.stationId;}
        public String getOperator() {return this.operator;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Float getTotalEnergyDelivered() {return this.totalEnergyDelivered;}
        public Integer getNrOfActivePoints() {return this.nrOfActivePoints;}
        @JsonIgnore
        public String getSessionsSummaryList() {return this.sessionsSummaryList.toString();}*/
        @Override
        @JsonIgnore
        public List<Object> getList() {
            List<Object> toRet = new ArrayList<>();
            for (PointObject po : this.sessionsSummaryList) {
                toRet.add(new CsvSessStationObject(this, po));
            }
            return toRet;
        }
    }
    
    public class NearbyStationObject {
        @JsonProperty("position")
        private List<Double> pos;
        @JsonProperty("label")
        private String label;
        @JsonProperty("time")
        private String time;
        @JsonProperty("condition")
        private String cond;
        @JsonProperty("station_id")
        private Integer id;

        @JsonIgnore
        public Boolean isFull() {
            return "isfull".equals(this.cond);
        }

        NearbyStationObject(ChargingStation cStation) {
            this.pos = new ArrayList<Double>();
            this.pos.add(cStation.getLatitude());
            this.pos.add(cStation.getLongitude());
            this.label = cStation.getAddressLine();
            this.time = "Unknown";
            
            Integer level1 = 0;
            Integer level2 = 0;
            Integer level3 = 0;

            List<ChargingPoint> cPoints =  cPointRepo.findBycStation(cStation);
            for (int i = 0; i < cPoints.size(); i++) {
                ChargingPoint cP = cPoints.get(i);
                if (cP.getChargerType() == 1 && cP.isOccupied() == false) level1++;
                if (cP.getChargerType() == 2 && cP.isOccupied() == false) level2++;
                if (cP.getChargerType() == 3 && cP.isOccupied() == false) level3++;
            }
            log.info("1: "+ level1.toString() + " 2: " + level2.toString() + " 3: " + level3.toString());
            if (level1 + level2 + level3 == 1)
            {
                if (level1 == 1) {this.cond = "1 Type-1 charger available";}
                if (level2 == 1) {this.cond = "1 Type-2 charger available";}
                if (level3 == 1) {this.cond = "1 Type-3 charger available";}
            }
            else if (level1 + level2 + level3 == 0) 
            {
                // station is fully occupied
                this.cond = "isfull";
            }
            else
            {
                this.cond = "";
                if (level1 != 0) {this.cond += level1.toString() + " Type-1, ";}
                if (level2 != 0) {this.cond += level2.toString() + " Type-2, ";}
                if (level3 != 0) {this.cond += level3.toString() + " Type-3, ";}
                this.cond = this.cond.substring(0, this.cond.length() -2);
                this.cond += " chargers available";
            }
            log.info(this.cond);

            this.id = cStation.getId();
        }
    }

    public class StationObject {
        @JsonProperty("StationID")
        private Integer stationId;
        @JsonProperty("TotalChargingPoints")
        private Integer nrOfChargingPoints;
        @JsonProperty("Longitude")
        private Double longitude;
        @JsonProperty("Latitude")
        private Double latitude;
        @JsonProperty("AddressLine")
        private String addressLine;
        @JsonProperty("AdminID")
        private Integer adminId;
        @JsonProperty("EnergyProviderID")
        private Integer eProviderId;

        StationObject (ChargingStation cStation) {
            this.stationId = cStation.getId();
            this.nrOfChargingPoints = cStation.getNrOfChargingPoints();
            this.latitude = cStation.getLatitude();
            this.longitude = cStation.getLongitude();
            this.addressLine = cStation.getAddressLine();
            this.adminId = cStation.getAdmin().getId();
            this.eProviderId = cStation.getEnergyProvider().getId();
        }
    }

    @Override
    public SessStationObject sessionsPerStation(
     Integer stationId, String dateFrom, String dateTo) throws NoDataException {
        Timestamp queryDateFrom = Utilities.timestampFromString(
         dateFrom, Utilities.DATE_FORMAT);
        Timestamp queryDateTo = Utilities.timestampFromString(
         dateTo, Utilities.DATE_FORMAT);
        log.info("Fetching ChargingStation entry...");
        ChargingStation queryStation = cStationRepo.findById(stationId)
         .orElseThrow(()-> new ChargingStationNotFoundException(stationId));
        log.info("Fetching ChargingData");
        List<ChargingPoint> cPoints =  cPointRepo
         .findBycStation(queryStation);
        if (cPoints.isEmpty()) throw new NoDataException();
        return new SessStationObject(queryDateFrom, queryDateTo,
        queryStation, cPoints);
    }

    @Override
    public List<NearbyStationObject> nearbyStations(Double latitude, 
    Double longitude, Double rad) throws NoDataException {
        List<NearbyStationObject> nearbyStations = new ArrayList<>();
        List<ChargingStation> queryStations;
        NearbyStationObject tempStation;
        Integer count = 0;
        do { 
            count++;   
            queryStations = cStationRepo.findByLatitudeBetweenAndLongitudeBetween
            (latitude - rad, latitude + rad, longitude - rad, longitude + rad);
            for (int i = 0; i < queryStations.size(); i++)
            {
                tempStation = new NearbyStationObject(queryStations.get(i));
                if (tempStation.isFull()) continue;
                nearbyStations.add(tempStation);
            }
            rad *= 2;
        } while (nearbyStations.isEmpty() && count != 4);
        if (nearbyStations.isEmpty()) throw new NoDataException();
        return nearbyStations;
    };

    @Override
    public StationObject createStation(Double lat, Double lon, String address,
    Integer adminId, Integer eProviderId) throws NoDataException {
        log.info("Creating Charging Station...");
        Admin admin = cAdminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        EnergyProvider eProvider = cEnergyProviderRepo.findById(eProviderId)
         .orElseThrow(()-> new EnergyProviderNotFoundException(eProviderId));
        ChargingStation station = new ChargingStation(0, lon, lat, address, admin, eProvider);
        ChargingStation createdStation = cStationRepo.save(station);
        return new StationObject(createdStation);
    }

    @Override
    public StationObject readStation(Integer stationId) throws NoDataException {
        log.info("Reading Charging Station...");
        ChargingStation queryStation = cStationRepo.findById(stationId)
         .orElseThrow(()-> new ChargingStationNotFoundException(stationId));
        return new StationObject(queryStation);
    }

    @Override
    public StationObject updateStation(Integer stationId, Double lat, Double lon, String address,
    Integer adminId, Integer eProviderId) throws NoDataException {
        log.info("Updating Charging Station...");
        ChargingStation station = cStationRepo.findById(stationId)
        .orElseThrow(()-> new ChargingStationNotFoundException(stationId));
        Admin admin = cAdminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        EnergyProvider eProvider = cEnergyProviderRepo.findById(eProviderId)
         .orElseThrow(()-> new EnergyProviderNotFoundException(eProviderId));
        station.setLongitude(lon);
        station.setLatitude(lat);
        station.setAddressLine(address);
        station.setAdmin(admin);
        station.setEnergyProvider(eProvider);
        ChargingStation updatedStation = cStationRepo.save(station);
        return new StationObject(updatedStation);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteStation(Integer stationId) throws NoDataException {
        log.info("Deleting Charging Station...");
        ChargingStation station = cStationRepo.findById(stationId)
        .orElseThrow(()-> new ChargingStationNotFoundException(stationId));

        /* Delete all charging points and set charging point id to null in charging sessions */
        for (ChargingPoint point: cPointRepo.findBycStation(station)) {
            List<ChargingSession> cSessList = cSessRepo.findByChargingPoint(point);
            
            for (ChargingSession cSess: cSessList)
            {
                cSess.setChargingPoint(null);
                cSessRepo.save(cSess);
            }
            
            cPointRepo.deleteById(point.getId());
        }

        cStationRepo.deleteById(stationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<StationObject> getAdminStations(Integer adminId) throws NoDataException {
        List<StationObject> toRet = new ArrayList<>();
        Admin admin = cAdminRepo.findById(adminId).orElseThrow(
            () -> new AdminNotFoundException(adminId));
        List<ChargingStation> queryStations = cStationRepo.findByAdmin(admin);
        if (queryStations.isEmpty()) throw new NoDataException();
        for (ChargingStation cStation : queryStations) {
            toRet.add(new StationObject(cStation));
        }
        return toRet;        
    }

    public class EnergySumObject {
        @JsonProperty("energySum")
        private Float sum;
        @JsonProperty("stationList")
        private List<NearbyStationObject> stations;

        EnergySumObject (Float sum, List<NearbyStationObject> nearStations) {
            this.sum = sum; this.stations = nearStations;
        }
    }

    @Override
    public EnergySumObject getAreaStationSum(Double latitude, Double longitude, Double rad, Integer adminId,
    String dateFrom, String dateTo) 
    throws NoDataException {
        Admin admin = cAdminRepo.findById(adminId).orElseThrow(() -> new AdminNotFoundException(adminId));
        List<ChargingStation> queryStations = cStationRepo.findByLatitudeBetweenAndLongitudeBetweenAndAdmin(
            latitude - rad, latitude + rad, longitude - rad, longitude + rad, admin);
        if (queryStations.isEmpty()) throw new NoDataException();
        List<NearbyStationObject> toRetList = new ArrayList<>();
        Float sum = 0.0f, tempEnergy;
        Timestamp queryDateFrom = Utilities.timestampFromString(dateFrom, Utilities.DATE_FORMAT);
        Timestamp queryDateTo = Utilities.timestampFromString(dateTo, Utilities.DATE_FORMAT);
        List<ChargingPoint> tempPoints;
        for (ChargingStation cStation : queryStations) {
            toRetList.add(new NearbyStationObject(cStation));
            tempPoints = cPointRepo.findBycStation(cStation);
            for (ChargingPoint cPoint : tempPoints) {
                tempEnergy = cSessRepo.totalEnergyDelivered(queryDateFrom, queryDateTo, cPoint); 
                sum = tempEnergy == null ? sum : sum + tempEnergy;     
            }
        }
        return new EnergySumObject(sum, toRetList);
    }

}
