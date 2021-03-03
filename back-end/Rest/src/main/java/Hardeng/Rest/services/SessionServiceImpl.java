package Hardeng.Rest.services;

import java.io.Console;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import Hardeng.Rest.exceptions.ChargingPointNotFoundException;
import Hardeng.Rest.exceptions.PricePolicyNotFoundException;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.CarNotFoundException;
import Hardeng.Rest.exceptions.CarDriverNotFoundException;
import Hardeng.Rest.exceptions.ChargingSessionNotFoundException;
import Hardeng.Rest.exceptions.ChargingStationNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.models.Car;
import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.repositories.PricePolicyRepository;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.repositories.CarRepository;
import Hardeng.Rest.repositories.CarDriverRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.services.EVServiceImpl.PricePolicyRef;

@Service
public class SessionServiceImpl implements SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    private final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private PricePolicyRepository pPolicyRepo;
    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;
    @Autowired
    private CarDriverRepository cCarDriverRepo;
    @Autowired
    private DriverRepository cDriverRepo;
    @Autowired
    private CarRepository cCarRepo;
    @Autowired
    private ChargingStationRepository cStationRepo;

    public class SessionObject {
        @JsonProperty("SessionID")
        private Integer sessionId;
        @JsonProperty("StartedOn")
        private String startedOn;
        @JsonProperty("FinishedOn")
        private String finishedOn;
        @JsonProperty("EnergyDelivered")
        private Float energyDelivered;
        @JsonProperty("Payment")
        private String payment;
        @JsonProperty("ChargingPointId")
        private Integer chargingPointId;
        @JsonProperty("PricePolicyId")
        private Integer pricePolicyId;
        @JsonProperty("CarId")
        private Integer carId;
        @JsonProperty("DriverId")
        private Integer driverId;

        SessionObject (ChargingSession session) {
            this.sessionId = session.getSessionId();

            Date dateS = new Date();
            dateS.setTime(session.getStartedOn().getTime());
            this.startedOn = TIMESTAMP_FORMAT.format(dateS);

            Date dateF = new Date();
            dateF.setTime(session.getFinishedOn().getTime());
            this.finishedOn = TIMESTAMP_FORMAT.format(dateF);

            this.energyDelivered = session.getEnergyDelivered();
            this.payment = session.getPayment();
            this.chargingPointId = session.getChargingPoint().getId();
            this.pricePolicyId = session.getPricePolicy().getID();
            this.carId = session.getCarDriver().getCar().getId();
            this.driverId = session.getCarDriver().getDriver().getID();
        }
    }

    @Override
    public SessionObject createSession(String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException {
        log.info("Creating Charging Session...");
        ChargingPoint point = cPointRepo.findById(chargingPointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(chargingPointId));
        PricePolicy policy = pPolicyRepo.findById(pricePolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pricePolicyId));
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        CarDriver carDriver = cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
         .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));
        ChargingSession session = new ChargingSession();
        session = new ChargingSession(session.parseTimestamp(startedOn), session.parseTimestamp(finishedOn),
        energyDelivered, payment, point, policy, carDriver);
        ChargingSession createdSession = cSessRepo.save(session);
        return new SessionObject(createdSession);
    }
    
    @Override
    public SessionObject readSession(Integer sessionId) throws NoDataException {
        log.info("Reading Charging Session...");
        ChargingSession querySession = cSessRepo.findById(sessionId)
         .orElseThrow(()-> new ChargingSessionNotFoundException(sessionId));
        return new SessionObject(querySession);
    }


    @Override
    public SessionObject updateSession(Integer sessionId, String startedOn, String finishedOn, Float energyDelivered,
    String payment, Integer chargingPointId, Integer pricePolicyId, Integer carId, Integer driverId) throws NoDataException {
        log.info("Updating Charging Session...");
        ChargingSession session = cSessRepo.findById(sessionId)
         .orElseThrow(()-> new ChargingSessionNotFoundException(sessionId));
        
        session.setStartedOn(session.parseTimestamp(startedOn));
        session.setFinishedOn(session.parseTimestamp(finishedOn));
        session.setEnergyDelivered(energyDelivered);
        session.setPayment(payment);

        ChargingPoint point = cPointRepo.findById(chargingPointId)
         .orElseThrow(()-> new ChargingPointNotFoundException(chargingPointId));
        PricePolicy policy = pPolicyRepo.findById(pricePolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pricePolicyId));
        Driver queryDriver = cDriverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        Car queryCar = cCarRepo.findById(carId)
         .orElseThrow(()-> new CarNotFoundException(carId));
        CarDriver carDriver = cCarDriverRepo.findByDriverAndCar(queryDriver, queryCar)
         .orElseThrow(()-> new CarDriverNotFoundException(queryDriver.getID(), queryCar.getId()));
        
        session.setChargingPoint(point);
        session.setPricePolicy(policy);
        session.setCarDriver(carDriver);

        ChargingSession updatedSession = cSessRepo.save(session);
        return new SessionObject(updatedSession);
    }

    @Override
    public ResponseEntity<Object> deleteSession(Integer sessionId) throws NoDataException {
        log.info("Deleting Charging Session...");
        cSessRepo.deleteById(sessionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Object> initiateSession(Integer stationId) throws InternalServerErrorException {
        ChargingPoint retPoint = null;
        ChargingStation cStation = cStationRepo.findById(stationId).orElseThrow(
            () -> new ChargingStationNotFoundException(stationId));
        List<ChargingPoint> cPoints = cPointRepo.findBycStation(cStation);
        // noteworthy: Bar some amazingly sad race conditions, there will
        // always be a point that is not occupied since this always takes place
        // after a request on NearbyStations, which fetches only available stations
        for (ChargingPoint iterPoint : cPoints) {
            if (!iterPoint.isOccupied()) {
                retPoint = iterPoint;
                break;
            }
        }
        // still checking, never hurts, but internal server error is thrown
        // cause a race condition is most probably at fault
        if (retPoint == null) throw new InternalServerErrorException();
        retPoint.setIsOccupied();
        cPointRepo.save(retPoint);
        return ResponseEntity.ok(retPoint.getId());
    }

    @Override 
    public void releasePoint(Integer pointId) throws InternalServerErrorException {
        ChargingPoint cPoint = cPointRepo.findById(pointId).orElseThrow(
            () -> new ChargingPointNotFoundException(pointId));
        if (!cPoint.isOccupied()) throw new InternalServerErrorException(); //never hurts to check :)
        cPoint.resetIsOccupied(); // "release" the chargingPoint
        cPointRepo.save(cPoint); // write changes in database
        return;
    }
}
