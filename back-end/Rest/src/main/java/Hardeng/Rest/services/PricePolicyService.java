package Hardeng.Rest.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.PricePolicyServiceImpl.PricePolicyObject;
import Hardeng.Rest.services.PricePolicyServiceImpl.DriverPolicyObject;

/** Price Policy controller business logic definitions */
public interface PricePolicyService {

    /** Creates a new PricePolicy in persistent data storage
     * @param kWh
     * @param costPerKWh
     * @param adminId
     * @return DTO with information about PricePolicy created
     * @throws NoDataException
     */
    PricePolicyObject createPricePolicy(Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException;

    /** Reads a PricePolicy's info by id
     * @param pPolicyId
     * @return DTO with information of PricePolicy requested
     * @throws NoDataException
     */
    PricePolicyObject readPricePolicy(Integer pPolicyId) throws NoDataException;

    /** Updates a PricePolicy's info by id
     * @param pPolicyId
     * @param kWh
     * @param costPerKWh
     * @param adminId
     * @return DTO with updated information of PricePolicy 
     * @throws NoDataException
     */
    PricePolicyObject updatePricePolicy(Integer pPolicyId, Integer kWh, Float costPerKWh, Integer adminId) throws NoDataException;
    
    /** Delets a PricePolicy's data from persistent storage
     * @param pPolicyId
     * @return HTTP Response Entity with proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> deletePricePolicy(Integer pPolicyId) throws NoDataException;

    /** Adds a relation between specified driver and PricePolicy
     * @param pPolicyId
     * @param driverId
     * @return HTTP Response Entity with proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> addPricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException;

    /** Removes a relation between specified driver and PricePolicy
     * @param pPolicyId
     * @param driverId
     * @return HTTP Response Entity with proper return code
     * @throws NoDataException
     */
    ResponseEntity<Object> removePricePolicyDriver(Integer pPolicyId, Integer driverId) throws NoDataException;

    /** Gets all PricePolicies that a Driver "has"
     * @param driverId
     * @return DTO with requested information
     * @throws NoDataException
     */
    DriverPolicyObject getAllPricePolicyDriver(Integer driverId) throws NoDataException;

    /** Gets all PricePolicies that an Admin "offers"
     * @param adminId
     * @return List of DTOs with PricePolicy info
     * @throws NoDataException
     */
    List<PricePolicyObject> getAdminPricePolicies(Integer adminId) throws NoDataException;
}