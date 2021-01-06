package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {}