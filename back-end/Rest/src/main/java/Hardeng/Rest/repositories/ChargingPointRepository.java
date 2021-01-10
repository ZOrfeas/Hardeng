package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingStation;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Integer> {

    /**
     * Returns Charging Points at a Charging Station {@code cStation}
     * @param cStation ChargingStation in question
     * @return List of charging points
     */
    List<ChargingPoint> findBycStation(ChargingStation cStation);
}
