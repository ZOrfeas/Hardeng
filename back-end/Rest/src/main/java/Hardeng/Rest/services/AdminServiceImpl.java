package Hardeng.Rest.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBeanBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import Hardeng.Rest.config.auth.SecurityConfig;
import Hardeng.Rest.Utilities;
import Hardeng.Rest.Utilities.CsvObject;
import Hardeng.Rest.config.auth.UserDetailsServiceImpl;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.exceptions.CarDriverNotFoundException;
import Hardeng.Rest.exceptions.CarNotFoundException;
import Hardeng.Rest.exceptions.ChargingPointNotFoundException;
//import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.AdminNotFoundException;
import Hardeng.Rest.exceptions.InternalServerErrorException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.exceptions.PricePolicyNotFoundException;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.Car;
import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.CarDriverRepository;
import Hardeng.Rest.repositories.CarRepository;
import Hardeng.Rest.repositories.ChargingPointRepository;
import Hardeng.Rest.repositories.ChargingSessionRepository;
import Hardeng.Rest.repositories.ChargingStationRepository;
import Hardeng.Rest.repositories.DriverRepository;
import Hardeng.Rest.repositories.PricePolicyRepository;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private ChargingSessionRepository sessionRepo;
    @Autowired
    private DriverRepository driverRepo;
    @Autowired
    private CarDriverRepository carDriverRepo;
    @Autowired
    private ChargingPointRepository cPointRepo;
    @Autowired
    private PricePolicyRepository pPolicyRepo;
    @Autowired
    private CarRepository carRepo;
    @Autowired
    private UserDetailsServiceImpl udsi;
    @Autowired
    private ChargingStationRepository cStationRepo;
    @Autowired
    private PasswordEncoder encoder;

    public static class StatusObject implements CsvObject{
        @JsonProperty("status")
        @CsvBindByName
        private String status;
        public StatusObject(String status) {
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
    
    // @Autowired
    // private DataSource dataSource;
    // the above may also be used to getConnection and run a ping on it
    // may be A LOT CLEANER
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
        
        log.info("Deleting all existing sessions...");
        sessionRepo.deleteAll();
        log.info("Sessions deleted.");

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

    
    @Override
    public StatusObject userMod(String driverName, String username, String password, String role, String email ) throws BadRequestException
    {
      switch(role)
      {
        case(SecurityConfig.driverRole):
            throw new BadRequestException();
            
            
        case("unregistered"):
        case(SecurityConfig.stationAdminRole):
            
            if(driverRepo.findByUsername(username).isPresent()) throw new BadRequestException();
            Driver newDriver = udsi.makeDriver(driverName, username, password, email);
            driverRepo.save(newDriver);
            break;
        case(SecurityConfig.masterAdminRole):
            if(adminRepo.findByUsername(username).isPresent()) throw new BadRequestException();
            Admin newAdmin = udsi.makeAdmin(username, password);
            adminRepo.save(newAdmin);
            break;
        default: throw new BadRequestException();       
      }
      return new StatusObject("Successful Sign Up");

    }

    public static class SessionStatsObject {
        @JsonProperty("SessionsInUploadedFile")
        private Integer sessionsInUploadedFile;
        @JsonProperty("SessionsImported")
        private long sessionsImported;
        @JsonProperty("TotalSessionsInDatabase")
        private long totalSessionsInDatabase;
        
        public SessionStatsObject(Integer sessInFile, long sessImported, long totalSess) {
            this.sessionsInUploadedFile = sessInFile;
            this.sessionsImported = sessImported;
            this.totalSessionsInDatabase = totalSess;
        }

        public Integer getSessionsInUploadedFile() {return this.sessionsInUploadedFile;}
        public long getSessionsImported() {return this.sessionsImported;}
        public long getTotalSessionsInDatabase() {return this.totalSessionsInDatabase;}

    }

    public static class SessionsImportObject {
        @CsvBindByName(column = "startedOn")
        private String startedOn;
        @CsvBindByName(column = "finishedOn")
        private String finishedOn;
        @CsvBindByName(column = "energyDelivered")
        private Float energyDelivered;
        @CsvBindByName(column = "payment")
        private String payment;
        @CsvBindByName(column = "chargingPointId")
        private Integer chargingPointId;
        @CsvBindByName(column = "pricePolicyId")
        private Integer pricePolicyId;
        @CsvBindByName(column = "driverId")
        private Integer driverId;
        @CsvBindByName(column = "carId")
        private Integer carId;

        public String getStartedOn() {return this.startedOn;}
        public String getFinishedOn() {return this.finishedOn;}
        public Float getEnergyDelivered() {return this.energyDelivered;}
        public String getPayment() {return this.payment;}
        public Integer getChargingPointId() {return this.chargingPointId;}
        public Integer getPricePolicyId() {return this.pricePolicyId;}
        public Integer getDriverId() {return this.driverId;}
        public Integer getCarId() {return this.carId;}

        public void setStartedOn(String newStart) {this.startedOn = newStart;}
        public void setFinishedOn(String newFinish) {this.finishedOn = newFinish;}
        public void setEnergyDelivered(Float newEnergy) {this.energyDelivered = newEnergy;}
        public void setPayment(String newPayment) {this.payment = newPayment;}
        public void setChargingPointId(Integer newId) {this.chargingPointId = newId;}
        public void setPricePolicyId(Integer newId) {this.pricePolicyId = newId;}
        public void setCarId(Integer newId) {this.carId = newId;}
        public void setDriverId(Integer newId) {this.driverId = newId;}
        // public ChargingSession transformChargingSession() {
        //     CarDriver cDriver;
        //     ChargingPoint cPoint = cPointRepo.findById();
        //     PricePolicy pPolicy;
        //     return new ChargingSession(this.startedOn, this.finishedOn, this.energyDelivered, this.payment, 
        //                                this.chargingPointId, this.pricePolicyId, carDriver)
        // }
    }

    // A semi-beautiful mess, if anyone feels up to it, feel free to make it normal,
    // or just gimme a shout and say "dude-bro wtf"
    @Override
    public SessionStatsObject sessionUpdate(MultipartFile file) {
        List<SessionsImportObject> toAdd;
        try {
        toAdd = new CsvToBeanBuilder(new InputStreamReader(file.getInputStream()))
            .withType(SessionsImportObject.class).build().parse();
        } catch(IOException e) {
            throw new InternalServerErrorException();
        }
        SessionStatsObject toRet;
        long pre,after;
        List<ChargingSession> converted = new ArrayList<>();
        toAdd.forEach((temp) -> converted.add(new ChargingSession(
            Utilities.timestampFromString(temp.getStartedOn(), Utilities.TIMESTAMP_FORMAT), 
            Utilities.timestampFromString(temp.getFinishedOn(),Utilities.TIMESTAMP_FORMAT), 
            temp.getEnergyDelivered(), 
            temp.getPayment(), 
            cPointRepo.findById(temp.getChargingPointId()).orElseThrow(
                ()->new ChargingPointNotFoundException(temp.getChargingPointId())), 
            pPolicyRepo.findById(temp.getPricePolicyId()).orElseThrow(
                ()->new PricePolicyNotFoundException(temp.getPricePolicyId())), 
            carDriverRepo.findByDriverAndCar(
                driverRepo.findById(temp.getDriverId()).orElseThrow(
                    ()->new DriverNotFoundException(temp.getDriverId())),
                carRepo.findById(temp.getCarId()).orElseThrow(
                    ()->new CarNotFoundException(temp.getCarId()))
                ).orElseThrow(()->new CarDriverNotFoundException(temp.getDriverId(), temp.getCarId()))
        )));
        pre = sessionRepo.count();
        sessionRepo.saveAll(converted);
        sessionRepo.flush();
        after = sessionRepo.count();

        toRet = new SessionStatsObject(toAdd.size(), after-pre, after);
        return toRet;
    }

    /* CRUD for Admin */
    public class AdminObject {
        @JsonProperty("AdminID")
        private Integer adminId;
        @JsonProperty("Username")
        private String username;
        @JsonProperty("Password")
        private String password;
        @JsonProperty("Email")
        private String email;
        @JsonProperty("CompanyName")
        private String companyName;
        @JsonProperty("CompanyPhone")
        private String companyPhone;
        @JsonProperty("CompanyLocation")
        private String companyLocation;

        AdminObject (Admin admin) {
            this.adminId = admin.getId();
            this.username = admin.getUsername();
            this.password = admin.getPassword();
            this.email = admin.getEmail();
            this.companyName = admin.getCompanyName();
            this.companyPhone = admin.getCompanyPhone();
            this.companyLocation = admin.getCompanyLocation();
        }
    }

    @Override
    public AdminObject createAdmin(String username, String password, String email,
    String companyName, String companyPhone, String companyLocation) throws NoDataException {
        log.info("Creating Admin...");
        Admin admin = udsi.makeAdmin(username, password, email, companyName, companyPhone, companyLocation);
        Admin createdAdmin = adminRepo.save(admin);
        return new AdminObject(createdAdmin);
    }

    @Override
    public AdminObject readAdmin(Integer adminId) throws NoDataException {
        log.info("Reading Admin...");
        Admin queryAdmin = adminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        return new AdminObject(queryAdmin);
    }

    @Override
    public AdminObject updateAdmin(Integer adminId, String username, String password, String email,
    String companyName, String companyPhone, String companyLocation) throws NoDataException {
        log.info("Updating Admin...");
        Admin queryAdmin = adminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        
        queryAdmin.setUsername(username);
        queryAdmin.setPassword(encoder.encode(password));
        queryAdmin.setEmail(email);
        queryAdmin.setCompanyName(companyName);
        queryAdmin.setCompanyPhone(companyPhone);
        queryAdmin.setCompanyLocation(companyLocation);
        
        Admin updatedAdmin = adminRepo.save(queryAdmin);
        return new AdminObject(updatedAdmin);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteAdmin(Integer adminId) throws NoDataException {
        log.info("Deleting Admin...");
        Admin admin = adminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));

        /* Set admin id in charging stations & price policy to null before deleting */
        List<ChargingStation> cStatList = cStationRepo.findByAdmin(admin);
        for (ChargingStation cStation: cStatList)
        {
            cStation.setAdmin(null);
            cStationRepo.save(cStation);
        }

        List<PricePolicy> pPolicyList = pPolicyRepo.findByAdmin(admin);
        for (PricePolicy pPolicy: pPolicyList)
        {
            pPolicy.setAdmin(null);
            pPolicyRepo.save(pPolicy);
        }
        
        adminRepo.deleteById(adminId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Object> fetchId(String username) throws BadRequestException {
        log.info("Fetching admin Id...");
        return ResponseEntity.ok(adminRepo.findByUsername(username).orElseThrow(
            () -> new BadRequestException()).getId());
    }
}

