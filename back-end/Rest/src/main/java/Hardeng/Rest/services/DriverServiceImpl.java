package Hardeng.Rest.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.config.auth.UserDetailsServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.DriverRepository;

@Service
public class DriverServiceImpl implements DriverService {
    private static final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    @Autowired
    private DriverRepository driverRepo;
    @Autowired
    private ChargingSessionRepository cSessRepo;
    @Autowired
    private UserDetailsServiceImpl udsi;
    @Autowired
    private PasswordEncoder encoder;
    
    /** DTO for Driver model data interactions */
    public class DriverObject {
        @JsonProperty("DriverID")
        private Integer driverId;
        @JsonProperty("DriverName")
        private String driverName;
        @JsonProperty("Username")
        private String username;
        @JsonProperty("Password")
        private String password;
        @JsonProperty("Email")
        private String email;
        @JsonProperty("BonusPoints")
        private Integer bonusPoints;
        @JsonProperty("CardId")
        private Long cardId;
        @JsonProperty("WalletId")
        private Long walletId;

        DriverObject (Driver driver) {
            this.driverId = driver.getID();
            this.driverName = driver.getDriverName();
            this.username = driver.getUsername();
            this.password = driver.getPassword();
            this.email = driver.getEmail();
            this.bonusPoints = driver.getBonusPoints();
            this.cardId = driver.getCardID();
            this.walletId = driver.getWalletID();
        }
    }

    @Override
    public DriverObject createDriver(String driverName, String username, String password, String email,
    Integer bonusPoints, Long cardId, Long walletId) throws NoDataException {
        log.info("Creating Driver...");
        Driver driver = udsi.makeDriver(driverName, username, password, email, bonusPoints, cardId, walletId);
        Driver createdDriver = driverRepo.save(driver);
        return new DriverObject(createdDriver);
    }

    @Override
    public DriverObject readDriver(Integer driverId) throws NoDataException {
        log.info("Reading Driver...");
        Driver queryDriver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        return new DriverObject(queryDriver);
    }

    @Override
    public DriverObject updateDriver(Integer driverId, String driverName, String username, String password,
    String email, Integer bonusPoints, Long cardId, Long walletId) throws NoDataException {
        log.info("Updating Driver...");
        Driver queryDriver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        
        queryDriver.setDriverName(driverName);
        queryDriver.setUsername(username);
        queryDriver.setPassword(encoder.encode(password));
        queryDriver.setEmail(email);
        queryDriver.setBonusPoints(bonusPoints);
        queryDriver.setCardID(cardId);
        queryDriver.setWalletID(walletId);
        Driver updatedDriver = driverRepo.save(queryDriver);
        return new DriverObject(updatedDriver);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteDriver(Integer driverId) throws NoDataException {
        log.info("Deleting Driver...");
        Driver driver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));

        /* Set car & driver ids in charging sessions to null before deleting */
        for (CarDriver carDriver: driver.getCars())
        {
            List<ChargingSession> cSessList = cSessRepo.findByCarDriver(carDriver);
            
            for (ChargingSession cSess: cSessList)
            {
                cSess.setCarDriver(null);
                cSessRepo.save(cSess);
            }
        }

        driverRepo.deleteById(driverId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Object> fetchId(String username) throws BadRequestException {
        log.info("Fetching driver Id...");
        return ResponseEntity.ok(driverRepo.findByUsername(username).orElseThrow(
            () -> new BadRequestException()).getID());
    }
}