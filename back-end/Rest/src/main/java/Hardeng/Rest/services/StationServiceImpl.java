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
import Hardeng.Rest.exceptions.ChargingStationNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;

@Service
public class StationServiceImpl implements StationService {
    private static final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    @Autowired
    private ChargingStationRepository cStationRepo;
    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;

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
        @Override
        public String toString() {
            return this.pointId +'|'+ this.pointSessions.toString() +'|'+ this.energyDelivered.toString();
        }
    }
    
    public class SessStationObject {
        @JsonProperty("StationID")
        @CsvBindByName
        private String stationId;
        @JsonProperty("Operator")
        @CsvBindByName
        private String operator;
        @JsonProperty("RequestTimeStamp")
        @CsvBindByName
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        @CsvBindByName
        private String periodFrom;
        @JsonProperty("PeriodTo")
        @CsvBindByName
        private String periodTo;
        @JsonProperty("TotalEnergyDelivered")
        @CsvBindByName
        private Float totalEnergyDelivered;
        @JsonProperty("NumberOfChargingSessions")
        @CsvBindByName
        private Integer nrOfChargingSessions;
        @JsonProperty("NumberOfActivePoints")
        @CsvBindByName
        private Integer nrOfActivePoints;
        @JsonProperty("SessionsSummaryList")
        @CsvBindByName(column = "POINTID|POINTSESSIONS|ENERGYDELIVERED")
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
        public String getStationId() {return this.stationId;}
        public String getOperator() {return this.operator;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Float getTotalEnergyDelivered() {return this.totalEnergyDelivered;}
        public Integer getNrOfActivePoints() {return this.nrOfActivePoints;}
        @JsonIgnore
        public String getSessionsSummaryList() {return this.sessionsSummaryList.toString();}
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
}
