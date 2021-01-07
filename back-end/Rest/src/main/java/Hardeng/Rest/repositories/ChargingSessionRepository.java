package Hardeng.Rest.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.ChargingSession;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Integer> {
    
    List<ChargingSession> findByChargingPointIdAndByStartedOnBetween(
        Integer cPointId, Timestamp dateFrom, Timestamp dateTo);
}