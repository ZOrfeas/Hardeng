package Hardeng.Rest.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.CarNotFoundException;
import Hardeng.Rest.exceptions.CarDriverNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.models.Car;
import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.repositories.CarRepository;
import Hardeng.Rest.repositories.CarDriverRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;

@Service
public class EVServiceImpl implements EVService {
    private static final Logger log = LoggerFactory.getLogger(StationServiceImpl.class);

    @Autowired
    private DriverRepository cDriverRepo;
    @Autowired
    private CarRepository cCarRepo;
    @Autowired
    private CarDriverRepository cCarDriverRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;
    
    public static class PricePolicyRef {
        private String companyName;
        private Integer kWh;
        private Float costPerKWh;

        PricePolicyRef(PricePolicy pPolicy) {
            this.companyName = pPolicy.getAdmin().getCompanyName();
            this.kWh = pPolicy.getKWh();
            this.costPerKWh = pPolicy.getCostPerKWh();
        }

        @Override
        public String toString() {
            return this.companyName + " [" + this.kWh.toString() + " kWh, " + 
                    this.costPerKWh.toString() + "€ per kWh]";
        }
    }

    public static class SessionObject {
        @JsonProperty("SessionIndex")
        @CsvBindByName
        private Integer sessionIndex;
        @JsonProperty("SessionID")
        @CsvBindByName
        private String sessionId;
        @JsonProperty("EnergyProvider")
        @CsvBindByName
        private String energyProvider;
        @JsonProperty("StartedOn")
        @CsvBindByName
        private String startedOn;
        @JsonProperty("FinishedOn")
        @CsvBindByName
        private String finishedOn;
        @JsonProperty("EnergyDelivered")
        @CsvBindByName
        private Float energyDelivered;
        @JsonProperty("PricePolicyRef")
        @CsvBindByName
        private String pricePolicyRef;
        @JsonProperty("CostPerKWh")
        @CsvBindByName
        private Float costPerKWh;
        @JsonProperty("SessionCost")
        @CsvBindByName
        private Float sessionCost;

        SessionObject(Integer index, ChargingSession cSession) {
            this.sessionIndex = index;
            this.sessionId = cSession.getSessionId().toString();
            this.energyProvider = cSession.getChargingPoint().getCStation().getEnergyProvider().getName();
            this.startedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getStartedOn());
            this.finishedOn = Utilities.TIMESTAMP_FORMAT.format(cSession.getFinishedOn());
            this.energyDelivered =  cSession.getEnergyDelivered();
            this.pricePolicyRef = new PricePolicyRef(cSession.getPricePolicy()).toString();
            this.costPerKWh = cSession.getPricePolicy().getCostPerKWh();
            this.sessionCost = this.costPerKWh * this.energyDelivered;
        }
        @Override
        public String toString() {
            return this.sessionIndex.toString() +'|'+ this.sessionId.toString() +'|'+ this.energyProvider +'|'+
            this.startedOn +'|'+ this.finishedOn +'|'+ this.pricePolicyRef +'|'+ this.energyDelivered.toString() +'|'+
            + '|' + this.costPerKWh +'|'+ this.sessionCost;
        }
    }

    public class SessEVObject {
        @JsonProperty("VehicleID")
        @CsvBindByName
        private String vehicleID;
        @JsonProperty("RequestTimeStamp")
        @CsvBindByName
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        @CsvBindByName
        private String periodFrom;
        @JsonProperty("PeriodTo")
        @CsvBindByName
        private String periodTo;
        @JsonProperty("TotalEnergyConsumed")
        @CsvBindByName
        private Float totalEnergyConsumed;
        @JsonProperty("NumberOfVisitedPoints")
        @CsvBindByName
        private Integer nrOfVisitedPoints;
        @JsonProperty("NumberOfVehicleChargingSessions")
        @CsvBindByName
        private Integer nrOfVehicleChargingSessions;
        @JsonProperty("VehicleChargingSessionsList")
        @CsvBindByName(column = "SESSIONINDEX|SESSIONID|ENERGYPROVIDER|STARTEDON|FINISHEDON|PRICEPOLICYREF|ENERGYDELIVERED|COSTPERKWH|SESSIONCOST")
        private List<SessionObject> sessionsSummaryList = new ArrayList<>();

        SessEVObject(Timestamp from, Timestamp to,
        CarDriver carDriver, List<ChargingSession> cSessions){
            this.vehicleID = carDriver.getDriver().getID().toString() + "-" + carDriver.getCar().getId().toString();
            this.requestTimeStamp = Utilities.TIMESTAMP_FORMAT.format(new Date());
            this.periodFrom = Utilities.TIMESTAMP_FORMAT.format(from);
            this.periodTo = Utilities.TIMESTAMP_FORMAT.format(to);
            this.nrOfVehicleChargingSessions = cSessions.size();
            this.totalEnergyConsumed = 0.0f;

            Set<Integer> points = new HashSet<Integer>();
            for (int i = 0; i < this.nrOfVehicleChargingSessions; i++) {
                SessionObject sessObject = new SessionObject(i+1, cSessions.get(i));
                this.sessionsSummaryList.add(sessObject);
                points.add(cSessions.get(i).getChargingPoint().getId());
                this.totalEnergyConsumed = this.totalEnergyConsumed + sessObject.energyDelivered;
            }
            this.nrOfVisitedPoints = points.size();
        }
        public String getVehicleId() {return this.vehicleID;}
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        public String getPeriodFrom() {return this.periodFrom;}
        public String getPeriodTo() {return this.periodTo;}
        public Float getTotalEnergyConsumed() {return this.totalEnergyConsumed;}
        public Integer getNrOfVisitedPoints() {return this.nrOfVisitedPoints;}
        public Integer getNrOfVehicleChargingSessions() {return this.nrOfVehicleChargingSessions;}
        @JsonIgnore
        public String getSessionsSummuryList() {return this.sessionsSummaryList.toString();}
    }
    
    @Override
    public SessEVObject sessionsPerEV(
     Integer driverId, Integer carId, String dateFrom, String dateTo) throws NoDataException {
        Timestamp queryDateFrom = Utilities.timestampFromString(
         dateFrom, Utilities.DATE_FORMAT);
        Timestamp queryDateTo = Utilities.timestampFromString(
         dateTo, Utilities.DATE_FORMAT);
         log.info("Fetching Driver entry...");
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        log.info("Fetching Car entry...");
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        log.info("Fetching CarDriver entry...");
        CarDriver queryCarDriver = cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
         .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));
        log.info("Fetching ChargingData");
        List<ChargingSession> cSessions =  cSessRepo
         .findByStartedOnBetweenAndCarDriver(queryDateFrom,
          queryDateTo, queryCarDriver);
        if (cSessions.isEmpty()) throw new NoDataException();
        return new SessEVObject(queryDateFrom, queryDateTo,
        queryCarDriver, cSessions);
    }
}
