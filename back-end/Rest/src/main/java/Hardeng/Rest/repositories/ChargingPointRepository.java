package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.ChargingPoint;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Integer> {}
