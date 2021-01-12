package Hardeng.Rest.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.Car;
import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.DriverRepository;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private ChargingSessionRepository sessionRepo;
    @Autowired
    private DriverRepository driverRepo;
    /** 
     * Deemed necessary to help with outputing
     * CSV and JSON types of this code. 
     */
    public static class StatusObject {
        @JsonProperty("status")
        @CsvBindByName
        private String status;
        StatusObject(String status) {
            this.status = status;
        }
        public String getStatus() {return this.status;}
    }
    
    @Override
    public StatusObject isHealthy() {
        log.info("Beginning healthcheck...");
        if (adminRepo.doAPing().isEmpty()) {
            log.error("Healthcheck failed.");
            return new StatusObject("failed");
        } else {
            log.info("Healthcheck successful.");
            return new StatusObject("OK");
        }
    }

    @Override
    public StatusObject resetSessions() {
        Optional<Admin> adminOptional;
        Admin defaultAdmin;
        log.info("Resetting sessions...");
        sessionRepo.deleteAll();
        log.info("Sessions cleared.");
        adminOptional = adminRepo.findByUsername("admin");
        if(adminOptional.isPresent()) {
            log.info("Default admin already exists. Deleting...");
            adminRepo.delete(adminOptional.get());
            log.info("Default admin deleted.");
        }
        defaultAdmin = new Admin("admin", "petrol4ever");
        log.info("Creating default admin...");
        adminRepo.save(defaultAdmin);
        return new StatusObject("OK");
    }

    public static class CarObject {
        @JsonProperty("CarId")
        @CsvBindByName
        private Integer id;
        @JsonProperty("BrandName")
        @CsvBindByName
        private String brandName;
        @JsonProperty("Model")
        @CsvBindByName
        private String model;

        CarObject(Car car) {
            this.id = car.getId();
            this.brandName = car.getBrandName();
            this.model = car.getModel();
        }
        @Override
        public String toString() {
            return this.id.toString() +'|'+ this.brandName +'|'+ this.model;
        }
    }

    public static class UserObject {
        @JsonProperty("Username")
        @CsvBindByName
        private String username;
        @JsonProperty("DriverName")
        @CsvBindByName
        private String driverName;
        @JsonProperty("Email")
        @CsvBindByName
        private String email;
        @JsonProperty("BonusPoints")
        @CsvBindByName
        private Integer bonusPoints;
        @JsonProperty("CarsOwnedList")
        @CsvBindByName(column = "ID|BRANDNAME|MODEL")
        private List<CarObject> carsOwnedList = new ArrayList<>();

        UserObject(Driver driver) {
            this.username = driver.getUsername();
            this.driverName = driver.getDriverName();
            this.email = driver.getEmail();
            this.bonusPoints = driver.getBonusPoints();
            for (CarDriver cd : driver.getCars()) 
                carsOwnedList.add(new CarObject(cd.getCar()));
        }
        public String getUsername() {return this.username;}
        public String getDriverName() {return this.driverName;}
        public String getEmail() {return this.email;}
        public Integer getBonusPoints() {return this.bonusPoints;}
        @JsonIgnore
        public String getCarsOwnedList() {return this.carsOwnedList.toString();}
    }

    @Override
    public UserObject getUserInfo(String username) throws NoDataException{
        Driver driver = driverRepo.findByUsername(username)
         .orElseThrow(()-> new DriverNotFoundException(username));
        return new UserObject(driver);
    }
}

