package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.ChargingStation;

@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {}
