package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.ChargingPoint;

public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Integer> {}
