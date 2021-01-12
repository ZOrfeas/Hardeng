package Hardeng.Rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    /**
     * Searches for a driver by username
     * 
     * @return Driver object or null
     */
    Optional<Driver> findByUsername(String username);

    /**
     * Searches for a driver by username and password
     * 
     * @return Driver object or null
     */
    Optional<Driver> findByUsernameAndPassword(String username, String password);
}