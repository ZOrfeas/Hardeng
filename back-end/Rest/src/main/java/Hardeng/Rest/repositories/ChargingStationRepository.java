package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.ChargingStation;
import Hardeng.Rest.models.EnergyProvider;


@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {
/** 
 *  Returns Energy Provider at a Charging Station {@code eProvider}
    * @param eProvider ChargingStation in question
    * @return List of charging points
*/

    List<ChargingStation> findByeProvider(EnergyProvider eProvider);

}
