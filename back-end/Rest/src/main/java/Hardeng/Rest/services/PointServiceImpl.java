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

import Hardeng.Rest.Utilities;
import Hardeng.Rest.exceptions.ChargingPointNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.services.EVServiceImpl.PricePolicyRef;

@Service
public class PointServiceImpl implements PointService {
    private static final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;

    public static class SessionObject {
        @JsonProperty("SessionIndex")
        @CsvBindByName
        private Integer sessionIndex;
        @JsonProperty("SessionID")
        @CsvBindByName
        private String sessionId;
        @JsonProperty("StartedOn")
        @CsvBindByName
        private String startedOn;
        @JsonProperty("FinishedOn")
        @CsvBindByName
        private String finishedOn;
        @JsonProperty("Protocol")
        @CsvBindByName
        private String protocol;
        @JsonProperty("EnergyDelivered")
        @CsvBindByName
        private Float energyDelivered;
        @JsonProperty("Payment")
        @CsvBindByName
        private String payment;
        @JsonProperty("VehicleType")
        @CsvBindByName
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
        public Integer getSessionIndex() {return this.sessionIndex;}
        public String getSessionId() {return this.sessionId;}
        public String getStartedOn() {return this.startedOn;}
        public String getFinishedOn() {return this.finishedOn;}
        public String getProtocol() {return this.protocol;}
        public Float getEnergyDelivered() {return this.energyDelivered;}
        public String getPayment() {return this.payment;}
        public String getVehicleType() {return this.vehicleType;}
        @Override
        public String toString() {
            return this.sessionIndex.toString() +'|'+ this.sessionId.toString() +'|'+
             this.startedOn +'|'+ this.finishedOn +'|'+ this.protocol +'|'+ this.energyDelivered.toString() +'|'+
             this.payment +'|'+ this.vehicleType;
        }

    }
    
    public static class SessPointObject {
        @JsonProperty("Point")
        @CsvBindByName
        private String point;
        @JsonProperty("PointOperator")
        @CsvBindByName
        private String pointOperator;
        @JsonProperty("RequestTimeStamp")
        @CsvBindByName
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        @CsvBindByName
        private String periodFrom;
        @JsonProperty("PeriodTo")
        @CsvBindByName
        private String periodTo;
        @JsonProperty("NumberOfChargingSessions")
        @CsvBindByName
        private Integer numberOfChargingSessions;
        @JsonProperty("ChargingSessionsList")
        @CsvBindByName(column = "SESSIONINDEX|SESSIONID|STARTEDON|FINISHEDON||PROTOCOLID|COSTPERKWH||PAYMENT|VEHICLETYPE")
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
        public String getPoint() {return this.point;}
        public String getPointOperator() {return this.pointOperator;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Integer getNumberOfChargingSessions() {return this.numberOfChargingSessions;}
        @JsonIgnore
        public String getChargingSessionsList() {return this.chargingSessionsList.toString();}
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
}
