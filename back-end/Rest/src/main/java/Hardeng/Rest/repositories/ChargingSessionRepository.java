package Hardeng.Rest.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.ChargingPoint;
import Hardeng.Rest.models.ChargingSession;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Integer> {
    
    /**
     * Fetches ChargingSessions that took place from
     * {@code dateFrom} to {@code dateTo} at Charging Point
     * {@code cPoint}
     * @param cPoint ChargingPoint in question
     * @param dateFrom start date
     * @param dateTo end date
     * @return List with charging points fulfilling query parameters
     */
    List<ChargingSession> findByChargingPointIdAndByStartedOnBetween(
        ChargingPoint cPoint, Timestamp dateFrom, Timestamp dateTo);
}