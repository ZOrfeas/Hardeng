package Hardeng.Rest.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.exceptions.ChargingPointNotFoundException;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;

@Service
public class PointServiceImpl implements PointService {
    private static final Logger log = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;

    public static class ProtocolObject {
        @JsonProperty("ProtocolID")
        @CsvBindByName
        private Integer protocolId;
        @JsonProperty("CostPerKWh")
        @CsvBindByName
        private Float costPerKWh;

        ProtocolObject(PricePolicy pPolicy) {
            this.protocolId = pPolicy.getID();
            this.costPerKWh = pPolicy.getCostPerKWh();
        }
    }

    public static class SessionObject {
        @JsonProperty("SessionIndex")
        @CsvBindByName
        private Integer sessionIndex;
        @JsonProperty("SessionID")
        @CsvBindByName
        private Integer sessionId;
        @JsonProperty("StartedOn")
        @CsvBindByName
        private String startedOn;
        @JsonProperty("FinishedOn")
        @CsvBindByName
        private String finishedOn;
        @JsonProperty("Protocol")
        @CsvBindByName
        private ProtocolObject protocol;
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
            this.sessionId = cSession.getSessionId();
            this.startedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getStartedOn());
            this.finishedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getFinishedOn());
            this.protocol = new ProtocolObject(cSession.getPricePolicy()); // note to self. Check this more !!!
            this.energyDelivered =  cSession.getEnergyDelivered();
            this.payment = cSession.getPayment();
            this.vehicleType = cSession.getCarDriver().getCar().getModel();
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
        @CsvBindByName
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
    }
    
    @Override
    public SessPointObject sessionsPerPoint(
     Integer pointId, String dateFrom, String dateTo) {
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
        return new SessPointObject(queryDateFrom, queryDateTo,
         queryPoint, cSessions);
    }
}
