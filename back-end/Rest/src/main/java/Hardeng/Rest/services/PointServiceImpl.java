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
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.Utilities.CsvObject;
import Hardeng.Rest.exceptions.ChargingPointNotFoundException;
import Hardeng.Rest.exceptions.ChargingStationNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.services.EVServiceImpl.PricePolicyRef;

@Service
public class PointServiceImpl implements PointService {
    private static final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;
    @Autowired
    private ChargingStationRepository cStatRepo;

    public static class SessionObject {
        @JsonProperty("SessionIndex")
        private Integer sessionIndex;
        @JsonProperty("SessionID")
        private String sessionId;
        @JsonProperty("StartedOn")
        private String startedOn;
        @JsonProperty("FinishedOn")
        private String finishedOn;
        @JsonProperty("Protocol")
        private String protocol;
        @JsonProperty("EnergyDelivered")
        private Float energyDelivered;
        @JsonProperty("Payment")
        private String payment;
        @JsonProperty("VehicleType")
        private String vehicleType;

        SessionObject(Integer index, ChargingSession cSession) {
            this.sessionIndex = index;
            this.sessionId = cSession.getSessionId().toString();
            this.startedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getStartedOn());
            this.finishedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getFinishedOn());
            this.protocol = new PricePolicyRef(cSession.getPricePolicy()).toString(); // note to self. Check this more !!!
            this.energyDelivered =  cSession.getEnergyDelivered();
            this.payment = cSession.getPayment();
            this.vehicleType = cSession.getCarDriver().getCar().getModel();
        }

    }

    public static class CsvSessPointObject {
        @CsvBindByName
        private String point;
        @CsvBindByName
        private String pointOperator;
        @CsvBindByName
        private String requestTimeStamp;
        @CsvBindByName
        private String periodFrom;
        @CsvBindByName
        private String periodTo;
        @CsvBindByName
        private Integer numberOfChargingSessions;
        @CsvBindByName
        private Integer sessionIndex;
        @CsvBindByName
        private String sessionId;
        @CsvBindByName
        private String startedOn;
        @CsvBindByName
        private String finishedOn;
        @CsvBindByName
        private String protocol;
        @CsvBindByName
        private Float energyDelivered;
        @CsvBindByName
        private String payment;
        @CsvBindByName
        private String vehicleType;
        CsvSessPointObject(SessPointObject pr, SessionObject so) {
            this.point = pr.point; this.pointOperator = pr.pointOperator;
            this.requestTimeStamp = pr.requestTimeStamp; this.periodFrom = pr.periodFrom;
            this.periodTo = pr.periodTo; this.numberOfChargingSessions = pr.numberOfChargingSessions;
            this.sessionIndex= so.sessionIndex; this.sessionId = so.sessionId; this.startedOn = so.startedOn;
            this.finishedOn = so.finishedOn; this.protocol = so.protocol; this.energyDelivered = so.energyDelivered;
            this.payment = so.payment; this.vehicleType = so.vehicleType;
        }
        public String getPoint() {return this.point;}
        public String getPointOperator() {return this.pointOperator;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Integer getNumberOfChargingSessions() {return this.numberOfChargingSessions;}
        public Integer getSessionIndex() {return this.sessionIndex;}
        public String getSessionId() {return this.sessionId;}
        public String getStartedOn() {return this.startedOn;}
        public String getFinishedOn() {return this.finishedOn;}
        public String getProtocol() {return this.protocol;}
        public Float getEnergyDelivered() {return this.energyDelivered;}
        public String getPayment() {return this.payment;}
        public String getVehicleType() {return this.vehicleType;}

    }
    
    public static class SessPointObject implements CsvObject {
        @JsonProperty("Point")
        private String point;
        @JsonProperty("PointOperator")
        private String pointOperator;
        @JsonProperty("RequestTimeStamp")
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        private String periodFrom;
        @JsonProperty("PeriodTo")
        private String periodTo;
        @JsonProperty("NumberOfChargingSessions")
        private Integer numberOfChargingSessions;
        @JsonProperty("ChargingSessionsList")
        private List<SessionObject> chargingSessionsList = new ArrayList<>();

        SessPointObject(Timestamp from, Timestamp to,
         ChargingPoint cPoint, List<ChargingSession> cSessions){
            this.point = cPoint.getId().toString();
            this.pointOperator = cPoint.getCStation().getAdmin().getCompanyName();
            this.requestTimeStamp = Utilities.TIMESTAMP_FORMAT.format(new Date());
            this.periodFrom = Utilities.TIMESTAMP_FORMAT.format(from);
            this.periodTo = Utilities.TIMESTAMP_FORMAT.format(to);
            this.numberOfChargingSessions = cSessions.size();
            for (int i = 0; i < this.numberOfChargingSessions; i++) {
                this.chargingSessionsList.add(new SessionObject(i+1, cSessions.get(i)));
            }
        }
        @Override
        @JsonIgnore
        public List<Object> getList() {
            List<Object> toRet = new ArrayList<>();
            for (SessionObject so : this.chargingSessionsList) {
                toRet.add(new CsvSessPointObject(this, so));
            }
            return toRet;
        }
    }
    
    public class PointObject {
        @JsonProperty("PointID")
        private Integer pointId;
        @JsonProperty("CurrentCondition")
        private Integer currentCondition;
        @JsonProperty("MaxEnergyOutput")
        private Integer maxEnergyOutput;
        @JsonProperty("IsOccupied")
        private Boolean isOccupied;
        @JsonProperty("ChargerType")
        private Integer chargerType;
        @JsonProperty("StationID")
        private Integer stationId;

        PointObject (ChargingPoint cPoint) {
            this.pointId = cPoint.getId();
            this.currentCondition = cPoint.getConditionInt();
            this.maxEnergyOutput = cPoint.getMaxOutput();
            this.isOccupied = cPoint.isOccupied();
            this.chargerType = cPoint.getChargerType();
            this.stationId = cPoint.getCStation().getId();
        }
    }

    @Override
    public SessPointObject sessionsPerPoint (
     Integer pointId, String dateFrom, String dateTo) throws NoDataException{
        Timestamp queryDateFrom = Utilities.timestampFromString(
         dateFrom, Utilities.DATE_FORMAT);
        Timestamp queryDateTo = Utilities.timestampFromString(
         dateTo, Utilities.DATE_FORMAT);
        log.info("Fetching ChargingPoint entry...");
        ChargingPoint queryPoint = cPointRepo.findById(pointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(pointId));
        log.info("Fetching ChargingSessions");
        List<ChargingSession> cSessions =  cSessRepo
         .findByStartedOnBetweenAndChargingPoint(queryDateFrom,
          queryDateTo, queryPoint);
        if (cSessions.isEmpty()) throw new NoDataException();
        return new SessPointObject(queryDateFrom, queryDateTo,
         queryPoint, cSessions);
    }

    @Override
    public PointObject createPoint(Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException {
        log.info("Creating Charging Point...");
        ChargingStation station = cStatRepo.findById(stationId)
         .orElseThrow(()-> new ChargingStationNotFoundException(stationId));
        ChargingPoint point = new ChargingPoint(condition, maxEnergy, isOccupied, chargerType, station);
        ChargingPoint createdPoint = cPointRepo.save(point);
        station.setNrOfChargingPoints(station.getNrOfChargingPoints()+1);
        cStatRepo.save(station);
        return new PointObject(createdPoint);
    }

    @Override
    public PointObject readPoint(Integer pointId) throws NoDataException {
        log.info("Reading Charging Point...");
        ChargingPoint queryPoint = cPointRepo.findById(pointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(pointId));
        return new PointObject(queryPoint);
    }

    @Override
    public PointObject updatePoint(Integer pointId, Integer condition, Integer maxEnergy, Boolean isOccupied,
    Integer chargerType, Integer stationId) throws NoDataException {
        log.info("Updating Charging Point...");
        ChargingPoint point = cPointRepo.findById(pointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(pointId));
        ChargingStation station = cStatRepo.findById(point.getCStation().getId())
         .orElseThrow(()-> new ChargingStationNotFoundException(point.getCStation().getId()));
        point.setConditionInt(condition);
        point.setMaxOutput(maxEnergy);
        if (isOccupied == true) {point.setIsOccupied();}
        else {point.resetIsOccupied();}
        point.setChargerType(chargerType);
        point.setCStation(station);
        ChargingPoint updatedPoint = cPointRepo.save(point);
        return new PointObject(updatedPoint);
    }

    @Override
    public ResponseEntity<Object> deletePoint(Integer pointId) throws NoDataException {
        log.info("Deleting Charging Point...");
        ChargingPoint point = cPointRepo.findById(pointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(pointId));
        ChargingStation station = cStatRepo.findById(point.getCStation().getId())
        .orElseThrow(()-> new ChargingStationNotFoundException(point.getCStation().getId()));
        cPointRepo.deleteById(pointId);
        station.setNrOfChargingPoints(station.getNrOfChargingPoints()-1);
        cStatRepo.save(station);
        return ResponseEntity.noContent().build();
    }
}
