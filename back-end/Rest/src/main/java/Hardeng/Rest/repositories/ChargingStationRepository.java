package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.ChargingStation;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {}
