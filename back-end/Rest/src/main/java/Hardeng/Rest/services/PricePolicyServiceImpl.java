package Hardeng.Rest.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import Hardeng.Rest.exceptions.PricePolicyNotFoundException;
import Hardeng.Rest.exceptions.AdminNotFoundException;
import Hardeng.Rest.exceptions.DriverNotFoundException;
import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.models.PricePolicy;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.repositories.PricePolicyRepository;
import Hardeng.Rest.repositories.AdminRepository;
import Hardeng.Rest.repositories.DriverRepository;

@Service
public class PricePolicyServiceImpl implements PricePolicyService {
    private static final Logger log = LoggerFactory.getLogger(PricePolicyServiceImpl.class);

    @Autowired
    private PricePolicyRepository pPolicyRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private DriverRepository driverRepo;

    /** DTO for PricePolicy model data interactions */
    public class PricePolicyObject {
        @JsonProperty("PricePolicyID")
        private Integer pPolicyId;
        @JsonProperty("KWh")
        private Integer kWh;
        @JsonProperty("CostPerKWh")
        private Float costPerKWh;
        @JsonProperty("AdminId")
        private Integer adminId;

        PricePolicyObject (PricePolicy policy) {
            this.pPolicyId = policy.getID();
            this.kWh = policy.getKWh();
            this.costPerKWh = policy.getCostPerKWh();
            if (policy.getAdmin() == null) {
                this.adminId = null;
            }
            else {
                this.adminId = policy.getAdmin().getId();
            }
        }
    }

    /** DTO with Driver-PricePolicy relation information */
    public class DriverPolicyObject {
        @JsonProperty("DriverID")
        private Integer driverID;
        @JsonProperty("PricePolicies")
        private List<PricePolicyObject> pricePolicies = new ArrayList<>();

        DriverPolicyObject(Driver driver, Set<PricePolicy> pricePolicies) {
            this.driverID = driver.getID(); 
            for (PricePolicy policy: pricePolicies) {
                PricePolicyObject pRef = new PricePolicyObject(policy);
                this.pricePolicies.add(pRef);
            }
        }
    }

    @Override
    public PricePolicyObject createPricePolicy(Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException {
        log.info("Creating Price Policy...");
        Admin admin = adminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        PricePolicy policy = new PricePolicy(kWh, costPerKWh, admin);
        PricePolicy createdPolicy = pPolicyRepo.save(policy);
        return new PricePolicyObject(createdPolicy);
    }
    
    @Override
    public PricePolicyObject readPricePolicy(Integer pPolicyId) throws NoDataException {
        log.info("Reading Price Policy...");
        PricePolicy policy = pPolicyRepo.findById(pPolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pPolicyId));
        return new PricePolicyObject(policy);
    }

    @Override
    public PricePolicyObject updatePricePolicy(Integer pPolicyId, Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException {
        log.info("Updating Price Policy...");
        PricePolicy policy = pPolicyRepo.findById(pPolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pPolicyId));
        Admin admin = adminRepo.findById(adminId)
         .orElseThrow(()-> new AdminNotFoundException(adminId));
        
        policy.setkWh(kWh);
        policy.setCostPerKWh(costPerKWh);
        policy.setAdmin(admin);

        PricePolicy updatedPolicy = pPolicyRepo.save(policy);
        return new PricePolicyObject(updatedPolicy);
    }

    @Override
    public ResponseEntity<Object> deletePricePolicy(Integer pPolicyId) throws NoDataException {
        log.info("Deleting Price Policy...");
        /*
         * Price Policy should not be deleted
         * When deleting just change adminId to null
         * This way it can still be obtained and used by drivers that have already purchased it
        */
        PricePolicy policy = pPolicyRepo.findById(pPolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pPolicyId));
        policy.setAdmin(null);
        pPolicyRepo.save(policy);
        return ResponseEntity.noContent().build();
    }

    /* Add PricePolicy to Driver */
    @Override
    public ResponseEntity<Object> addPricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException {
        log.info("Add Price Policy to Driver...");
        PricePolicy policy = pPolicyRepo.findById(pPolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pPolicyId));
        Driver driver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        driver.addPricePolicy(policy);
        driverRepo.save(driver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Remove PricePolicy to Driver */
    @Override
    public ResponseEntity<Object> removePricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException {
        log.info("Remove Price Policy to Driver...");
        PricePolicy policy = pPolicyRepo.findById(pPolicyId)
         .orElseThrow(()-> new PricePolicyNotFoundException(pPolicyId));
        Driver driver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        driver.removePricePolicy(policy);
        driverRepo.save(driver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Get all Price Policies of a driver */
    @Override
    public DriverPolicyObject getAllPricePolicyDriver(Integer driverId) throws NoDataException {
        Driver driver = driverRepo.findById(driverId)
         .orElseThrow(()-> new DriverNotFoundException(driverId));
        return new DriverPolicyObject(driver, driver.getPricePolicies());
    }

    @Override
    public List<PricePolicyObject> getAdminPricePolicies(Integer adminId) throws NoDataException {
        Admin admin = adminRepo.findById(adminId).orElseThrow(
            () -> new AdminNotFoundException(adminId));
        List<PricePolicy> queryPolicies = pPolicyRepo.findByAdmin(admin);
        if (queryPolicies.isEmpty()) throw new NoDataException();
        List<PricePolicyObject> toRet = new ArrayList<>();
        for (PricePolicy pPolicy : queryPolicies) {
            toRet.add(new PricePolicyObject(pPolicy));
        }
        return toRet;
    }

}
