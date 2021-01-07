package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.CarDriver;

public interface CarDriverRepository extends JpaRepository<CarDriver, Integer> {}