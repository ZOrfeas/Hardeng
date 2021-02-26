package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.PricePolicyServiceImpl.PricePolicyObject;
import Hardeng.Rest.services.PricePolicyServiceImpl.DriverPolicyObject;

public interface PricePolicyService {

    PricePolicyObject createPricePolicy(Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException;

    PricePolicyObject readPricePolicy(Integer pPolicyId) throws NoDataException;

    PricePolicyObject updatePricePolicy(Integer pPolicyId, Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException;
    
    ResponseEntity<Object> deletePricePolicy(Integer pPolicyId) throws NoDataException;

    ResponseEntity<Object> addPricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException;

    ResponseEntity<Object> removePricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException;

    DriverPolicyObject getAllPricePolicyDriver(Integer driverId) throws NoDataException;
}