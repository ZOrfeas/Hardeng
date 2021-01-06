package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.ChargingSession;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Integer> {}