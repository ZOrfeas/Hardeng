package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.PricePolicyService;
import Hardeng.Rest.services.PricePolicyServiceImpl.PricePolicyObject;
import Hardeng.Rest.services.PricePolicyServiceImpl.DriverPolicyObject;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PricePolicyController {
    private static final Logger log = LoggerFactory.getLogger(PricePolicyController.class);

    @Autowired
    PricePolicyService pPolicyService;

    /* CRUD for PricePolicy */
    public static class RequestPolicy {
        private Integer kiloWh;
        private Float costPerKWh;
        private Integer adminId;

        public Integer getKiloWh() {return this.kiloWh;}
        public Float getCostPerKWh() {return this.costPerKWh;}
        public Integer getAdminId() {return this.adminId;}

        public void setKiloWh(Integer kWh) {this.kiloWh = kWh;}
        public void setCostPerKWh(Float costPerKWh) {this.costPerKWh = costPerKWh;}
        public void setAdminId(Integer adminId) {this.adminId = adminId;}

    }

    @PostMapping(value = "/PricePolicy", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
    public PricePolicyObject createPricePolicy(@RequestBody RequestPolicy policy) {
        log.info("Create policy...");
        if (policy.getKiloWh() == null || policy.getCostPerKWh() == null || policy.getAdminId() == null) throw new BadRequestException();
        return pPolicyService.createPricePolicy(policy.getKiloWh(), policy.getCostPerKWh(), policy.getAdminId());
    }

    @GetMapping(value = "/PricePolicy/{pPolicyId}", produces = {"application/json"})
    public PricePolicyObject readPricePolicy(@PathVariable(name = "pPolicyId") Integer pPolicyId) {
        log.info("Read policy...");
        if (pPolicyId == null) throw new BadRequestException();
        return pPolicyService.readPricePolicy(pPolicyId);
    }

    
    @PutMapping(value = "/PricePolicy/{pPolicyId}", consumes=MediaType.APPLICATION_JSON_VALUE, produces = {"application/json"})
    public PricePolicyObject updatePricePolicy(@PathVariable(name = "pPolicyId") Integer pPolicyId, @RequestBody RequestPolicy policy) {
        log.info("Update policy...");
        if (policy.getKiloWh() == null || policy.getCostPerKWh() == null || policy.getAdminId() == null) throw new BadRequestException();
        return pPolicyService.updatePricePolicy(pPolicyId, policy.getKiloWh(), policy.getCostPerKWh(), policy.getAdminId());
    }

    @DeleteMapping(value = "/PricePolicy/{pPolicyId}")
    public ResponseEntity<Object> deletePricePolicy(@PathVariable(name = "pPolicyId") Integer pPolicyId) {
        log.info("Delete policy...");
        if (pPolicyId == null) throw new BadRequestException();
        return pPolicyService.deletePricePolicy(pPolicyId);
    }

    /* Add PricePolicy to Driver */
    @PostMapping(value = "/AddPricePolicy/{pPolicyId}/{driverId}")
    public ResponseEntity<Object> addPricePolicyDriver(@PathVariable(name = "pPolicyId") Integer pPolicyId, 
    @PathVariable(name = "driverId") Integer driverId) {
        log.info("Add policy to driver...");
        return pPolicyService.addPricePolicyDriver(pPolicyId, driverId);
    }

    /* Remove PricePolicy from Driver */
    @DeleteMapping(value = "/RemovePricePolicy/{pPolicyId}/{driverId}")
    public ResponseEntity<Object> removePricePolicyDriver(@PathVariable(name = "pPolicyId") Integer pPolicyId, 
    @PathVariable(name = "driverId") Integer driverId) {
        log.info("Remove policy from driver...");
        return pPolicyService.removePricePolicyDriver(pPolicyId, driverId);
    }

    /* Get all PricePolicies of a Driver */
    @GetMapping(value="/PricePoliciesPerDriver/{driverId}")
    public DriverPolicyObject getAllPricePolicyDriver(@PathVariable(name = "driverId") Integer driverId) {
        log.info("Get all price policies of  a driver...");
        return pPolicyService.getAllPricePolicyDriver(driverId);
    }
}