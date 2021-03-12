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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import Hardeng.Rest.Utilities;
import Hardeng.Rest.Utilities.CsvObject;
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
    
    /** Helper class for Price Policy info string creation */
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

    /** DTO with Session information */
    public static class SessionObject {
        @JsonProperty("SessionIndex")
        private Integer sessionIndex;
        @JsonProperty("SessionID")
        private String sessionId;
        @JsonProperty("EnergyProvider")
        private String energyProvider;
        @JsonProperty("StartedOn")
        private String startedOn;
        @JsonProperty("FinishedOn")
        private String finishedOn;
        @JsonProperty("EnergyDelivered")
        private Float energyDelivered;
        @JsonProperty("PricePolicyRef")
        private String pricePolicyRef;
        @JsonProperty("CostPerKWh")
        private Float costPerKWh;
        @JsonProperty("SessionCost")
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
    }
    
    /** CSV-serializable DTO with SessionsPerEV information */
    public class CsvSessEVObject {
        @CsvBindByName
        private String vehicleID;
        public String getVehicleID () {return this.vehicleID;}
        @CsvBindByName
        private String requestTimeStamp;
        public String getRequestTimeStamp() {return this.requestTimeStamp;}
        @CsvBindByName
        private String periodFrom;
        public String getPeriodFrom () {return this.periodFrom;}
        @CsvBindByName
        private String periodTo;
        public String getPeriodTo () {return this.periodTo;}
        @CsvBindByName
        private Float totalEnergyConsumed;
        public Float getTotalEnergyConsumed () {return this.totalEnergyConsumed;}
        @CsvBindByName
        private Integer nrOfVisitedPoints;
        public Integer getNrOfVisitedPoints () {return this.nrOfVisitedPoints;}
        @CsvBindByName
        private Integer nrOfVehicleChargingSessions;
        public Integer getNrOfVehicleChargingSessions () {return this.nrOfVehicleChargingSessions;}
        @CsvBindByName
        private Integer sessionIndex;
        public Integer getSessionIndex () {return this.sessionIndex;}
        @CsvBindByName
        private String sessionId;
        public String getSessionId () {return this.sessionId;}
        @CsvBindByName
        private String energyProvider;
        public String getEnergyProvider () {return this.energyProvider;}
        @CsvBindByName
        private String startedOn;
        public String getStartedOn () {return this.startedOn;}
        @CsvBindByName
        private String finishedOn;
        public String getFinishedOn () {return this.finishedOn;}
        @CsvBindByName
        private Float energyDelivered;
        public Float getEnergyDelivered () {return this.energyDelivered;}
        @CsvBindByName
        private String pricePolicyRef;
        public String getPricePolicyRef () {return this.pricePolicyRef;}
        @CsvBindByName
        private Float costPerKWh;
        public Float getCostPerKWh () {return this.costPerKWh;}
        @CsvBindByName
        private Float sessionCost;
        public Float getSessionCost () {return this.sessionCost;}
        CsvSessEVObject(SessEVObject parent, SessionObject so) {
            this.vehicleID = parent.vehicleID; this.requestTimeStamp = parent.requestTimeStamp;
            this.periodFrom = parent.periodFrom; this.periodTo = parent.periodTo;
            this.totalEnergyConsumed = parent.totalEnergyConsumed;
            this.nrOfVisitedPoints = parent.nrOfVisitedPoints;
            this.nrOfVehicleChargingSessions = parent.nrOfVehicleChargingSessions;
            this.sessionIndex = so.sessionIndex; this.sessionId = so.sessionId;
            this.energyProvider = so.energyProvider; this.startedOn = so.startedOn;
            this.finishedOn = so.finishedOn; this.energyDelivered = so.energyDelivered;
            this.pricePolicyRef = so.pricePolicyRef; this.costPerKWh = so.costPerKWh;
            this.sessionCost = so.sessionCost;
        }

    }
    
    /** DTO with SessionsPerEV information */
    public class SessEVObject implements CsvObject {
        @JsonProperty("VehicleID")
        private String vehicleID;
        @JsonProperty("RequestTimeStamp")
        private String requestTimeStamp;
        @JsonProperty("PeriodFrom")
        private String periodFrom;
        @JsonProperty("PeriodTo")
        private String periodTo;
        @JsonProperty("TotalEnergyConsumed")
        private Float totalEnergyConsumed;
        @JsonProperty("NumberOfVisitedPoints")
        private Integer nrOfVisitedPoints;
        @JsonProperty("NumberOfVehicleChargingSessions")
        private Integer nrOfVehicleChargingSessions;
        @JsonProperty("VehicleChargingSessionsList")
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
        @Override
        @JsonIgnore
        public List<Object> getList() {
            List<Object> toRet = new ArrayList<>();
            for (SessionObject so : this.sessionsSummaryList) {
                toRet.add(new CsvSessEVObject(this, so));
            }
            return toRet;
        }
    }
    
    /** DTO for EV model interaction */
    public class EVObject {
        @JsonProperty("DriverID")
        private Integer driverId;
        @JsonProperty("CarID")
        private Integer carId;
        @JsonProperty("CurrentCapacity")
        private Double currentCap;

        EVObject (CarDriver ev) {
            this.driverId = ev.getDriver().getID();
            this.carId = ev.getCar().getId();
            this.currentCap = ev.getCurrentCapacity();
        }
    }

    /** DTO for Driver EV relation interaction */
    public class DriverCarObject {
        @JsonProperty("DriverID")
        private Integer driverId;
        @JsonProperty("Cars")
        private List<CarObject> cars = new ArrayList<>();

        private class CarObject {
            @JsonProperty("CarID")
            private Integer carId;
            @JsonProperty("BrandName")
            private String brandName;
            @JsonProperty("Model")
            private String model;
            @JsonProperty("ChargerTypes")
            private String chargerTypes;
            @JsonProperty("MaxCapacity")
            private Integer maxCap;
            @JsonProperty("CurrentCapacity")
            private Double currentCap;

            CarObject(Car car, Double currentCap) {
                this.carId = car.getId();
                this.brandName = car.getBrandName();
                this.model = car.getModel();
                this.chargerTypes = createChargerTypeString(car.getacceptsType1(), car.getacceptsType2(), car.getacceptsType3());
                this.maxCap = car.getBatteryCapacity();
                this.currentCap = currentCap;
            }

            public String createChargerTypeString(Boolean level1, Boolean level2, Boolean level3) {
                String chargerTypeString = "Accepts: ";

                if (level1 == true) { chargerTypeString += "Type-1, ";}
                if (level2 == true) { chargerTypeString += "Type-2, ";}
                if (level3 == true) { chargerTypeString += "Type-3, ";}
                
                return chargerTypeString.substring(0, chargerTypeString.length() - 2);
            }
        }
        
        DriverCarObject(Driver driver, Set<CarDriver> cars) {
            this.driverId = driver.getID();
            for (CarDriver ev: cars) {
                CarObject carObj = new CarObject(ev.getCar(), ev.getCurrentCapacity());
                this.cars.add(carObj);
            }
        }
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

    @Override
    public EVObject createEV(Integer driverId, Integer carId, Double newCap) throws NoDataException {
        log.info("Creating EV...");
        Driver driver = cDriverRepo.findById(driverId)
        .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car car = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        CarDriver ev = new CarDriver(driver, car, newCap);
        CarDriver createdEV = cCarDriverRepo.save(ev);
        return new EVObject(createdEV);
    }

    @Override
    public EVObject readEV(Integer driverId, Integer carId) throws NoDataException {
        log.info("Reading EV...");
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        CarDriver queryEV = cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
        .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));
        return new EVObject(queryEV);
    }

    @Override
    public EVObject updateEV(Integer driverId, Integer carId, Double newCap) throws NoDataException {
        log.info("Updating EV...");
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        CarDriver queryEV = cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
        .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));
        queryEV.setDriver(queryDriver);
        queryEV.setCar(queryCar);
        queryEV.setCurrentCapacity(newCap);
        CarDriver updatedEV = cCarDriverRepo.save(queryEV);
        return new EVObject(updatedEV);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteEV(Integer driverId, Integer carId) throws NoDataException {
        log.info("Deleting EV...");
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        /**CarDriver queryEV = */cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
        .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));

        /* Set car & driver ids in charging sessions to null before deleting */
        for (CarDriver carDriver: queryDriver.getCars())
        {
            List<ChargingSession> cSessList = cSessRepo.findByCarDriver(carDriver);
            
            for (ChargingSession cSess: cSessList)
            {
                cSess.setCarDriver(null);
                cSessRepo.save(cSess);
            }
        }
        queryDriver.removeCar(queryCar);
        return ResponseEntity.noContent().build();
    }

    @Override
    public DriverCarObject getAllCarDriver(Integer driverId) throws NoDataException {
        Driver driver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        return new DriverCarObject(driver, driver.getCars());
    }
    
}
