package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {}
