package Hardeng.Rest.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingSession;
import Hardeng.Rest.models.CarDriver;

/** Charging Session model interface */
@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Integer> {
    
    /**
     * Fetches ChargingSessions that took place from
     * {@code dateFrom} to {@code dateTo} at Charging Point
     * {@code cPoint}
     * @param dateFrom start date
     * @param dateTo end date
     * @param cPoint ChargingPoint in question
     * @return List with charging points fulfilling query parameters
     */
    List<ChargingSession> findByStartedOnBetweenAndChargingPoint(
        Timestamp dateFrom, Timestamp dateTo, ChargingPoint cPoint);

    /**
     * Returns number of charging sessions at a Charging Point
     * {@code cPoint} that took place from {@code dateFrom}
     * to {@code dateTo}
     * @param dateFrom start date
     * @param dateTo end date
     * @param cPoint ChargingPoint in question
     * @return Integer returns number of charging sessions
     */
    Integer countByStartedOnGreaterThanEqualAndFinishedOnLessThanEqualAndChargingPoint(
        Timestamp dateFrom, Timestamp dateTo, ChargingPoint cPoint);

    /**
     * Returns total energy delivered by a Charging Point
     * {@code cPoint} from {@code dateFrom}
     * to {@code dateTo}
     * @param dateFrom start date
     * @param dateTo end date
     * @param cPoint ChargingPoint in question
     * @return Float returns total energy delivered
     */
    @Query("SELECT SUM(s.energyDelivered) FROM ChargingSession s " + 
            "WHERE s.startedOn >= :dateFrom AND s.finishedOn <= :dateTo AND s.chargingPoint = :cPoint")
    Float totalEnergyDelivered(Timestamp dateFrom, Timestamp dateTo, ChargingPoint cPoint);

    /** Queries for charging sessions within specified timeframe by specified driver and car
     * @param dateFrom Timeframe begin date
     * @param dateTo Timeframe end date
     * @param carDriver carDriver in question
     * @return List of charging sessions
     */
    List<ChargingSession> findByStartedOnBetweenAndCarDriver(Timestamp dateFrom, Timestamp dateTo, CarDriver carDriver);

    /** Queries for charging sessions by specified driver and car
     * @param carDriver carDriver in question
     * @return List of charging sessions
     */
    List<ChargingSession> findByCarDriver(CarDriver carDriver);

    /** Queries for charging sessions by specified charging point
     * @param cPoint charging point in question
     * @return List of charging sessions
     */
    List<ChargingSession> findByChargingPoint(ChargingPoint cPoint);
}