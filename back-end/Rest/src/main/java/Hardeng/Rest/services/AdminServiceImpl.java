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

import Hardeng.Rest.Utilities.CsvObject;
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
    

    public static class StatusObject implements CsvObject{
        @JsonProperty("status")
        @CsvBindByName
        private String status;
        StatusObject(String status) {
            this.status = status;
        }
        public String getStatus() {return this.status;}
        @Override
        @JsonIgnore
        public List<Object> getList() {
            List<Object> toRet = new ArrayList<>();
            toRet.add(this);
            return toRet;
        }
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
        private Integer id;
        @JsonProperty("BrandName")
        private String brandName;
        @JsonProperty("Model")
        private String model;

        CarObject(Car car) {
            this.id = car.getId();
            this.brandName = car.getBrandName();
            this.model = car.getModel();
        }
    }

    public static class CsvUserObject {
        @CsvBindByName(column = "Username")
        private String username;
        public String getUsername() {return this.username;}
        @CsvBindByName(column = "DriverName")
        private String driverName;
        public String getDriverName() {return this.driverName;}
        @CsvBindByName(column = "Email")
        private String email;
        public String getEmail() {return this.email;}        
        @CsvBindByName(column = "BonusPoints")
        private Integer bonusPoints;
        public Integer getBonusPoints() {return this.bonusPoints;}
        @CsvBindByName(column = "CarId")
        private Integer carId;
        public Integer getCarId() {return this.carId;}
        @CsvBindByName(column = "BrandName")
        private String brandName;
        public String getBrandName() {return this.brandName;}
        @CsvBindByName(column = "Model")
        private String model;
        public String getModel() {return this.model;}
        CsvUserObject(UserObject parent, CarObject co)
        {
            this.username = parent.username; this.driverName = parent.driverName;
            this.email = parent.email; this.bonusPoints = parent.bonusPoints;
            this.carId = co.id; this.brandName = co.brandName;
            this.model = co.model;
        }
    }
    public static class UserObject implements CsvObject{
        @JsonProperty("Username")
        private String username;
        @JsonProperty("DriverName")
        private String driverName;
        @JsonProperty("Email")
        private String email;
        @JsonProperty("BonusPoints")
        private Integer bonusPoints;
        @JsonProperty("CarsOwnedList")
        private List<CarObject> carsOwnedList = new ArrayList<>();

        UserObject(Driver driver) {
            this.username = driver.getUsername();
            this.driverName = driver.getDriverName();
            this.email = driver.getEmail();
            this.bonusPoints = driver.getBonusPoints();
            for (CarDriver cd : driver.getCars()) 
                carsOwnedList.add(new CarObject(cd.getCar()));
        }
        @Override
        @JsonIgnore
        public List<Object> getList() {
            List<Object> toRet = new ArrayList<>();
            for (CarObject co : this.carsOwnedList) {
                toRet.add(new CsvUserObject(this, co));
            }
            return toRet;
        }
    }

    @Override
    public UserObject getUserInfo(String username) throws NoDataException{
        Driver driver = driverRepo.findByUsername(username)
         .orElseThrow(()-> new DriverNotFoundException(username));
        return new UserObject(driver);
    }
}

