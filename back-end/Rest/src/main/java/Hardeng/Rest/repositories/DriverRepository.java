package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {}