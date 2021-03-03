package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.EnergyProvider;


@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {
    /** 
     *  Returns Charging Station by energyProvider {@code eProvider}
     * @param eProvider Energy Provider in question
     * @return List of charging stations
    */

    List<ChargingStation> findByeProvider(EnergyProvider eProvider);

    /** 
     * Returns List of Charging Stations based on 
     * latitude and longitude retrictions
     * @param latLow latitude lower limit
     * @param latUp latitude upper liit
     * @param lonLow longitude lower limit
     * @param lonUp longitude upperlimit
     * @return List of charging stations
     */

    List<ChargingStation> findByLatitudeBetweenAndLongitudeBetween(Double latLow,
    Double latUp, Double lonLow, Double lonUp);

    /** 
     *  Returns Charging Station by admin {@code admin}
     * @param admin Admin in question
     * @return List of charging stations
    */

    List<ChargingStation> findByAdmin(Admin admin);

    /**
     * Fetch stations in specified area belonging to specific admin
     * @param latLow latitude lower limit
     * @param latUp latitude upper liit
     * @param lonLow longitude lower limit
     * @param lonUp longitude upperlimit
     * @param admin Admin in question
     * @return List of charging stations
     */
    List<ChargingStation> findByLatitudeBetweenAndLongitudeBetweenAndAdmin(Double latLow,
    Double latUp, Double lonLow, Double lonUp, Admin admin);
}
