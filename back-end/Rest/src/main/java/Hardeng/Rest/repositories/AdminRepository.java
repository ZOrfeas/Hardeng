package Hardeng.Rest.repositories;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Hardeng.Rest.models.Admin;

/** Admin model interface */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    /**
     * Pings backend database system
     * @return Non-Empty Collection on success, empty on failure
     */
    @Query(value = "/* ping */ SELECT 1", nativeQuery = true)
    Collection<Object> doAPing();

    /**
     * finds an admin by username
     * @param username username of admin requested
     * @return Optional<Admin> Object with Admin in it if it exists
     */
    Optional<Admin> findByUsername(String username);

    /**
     * finds an admin by username and password
     * @param username username of admin requested
     * @param password password of admin requested
     * @return Optional<Admin> Object with Admin in it if it exists
     */
    Optional<Admin> findByUsernameAndPassword(String username, String password);

    /** Counts and returns an admin's total energy consumption during specified timeframe
     * @param dateFrom Timeframe begin datetime
     * @param dateTo Timeframe end datetime
     * @param admin Admin in question
     * @return Total energy consumption
     */
    @Query("SELECT SUM(sess.energyDelivered) FROM ChargingSession sess " +
           "WHERE sess.startedOn >= :dateFrom AND sess.finishedOn <= :dateTo " + 
           "AND sess.chargingPoint IN (SELECT point.id FROM ChargingPoint point " +
           "WHERE point.cStation IN (SELECT stat.id FROM ChargingStation stat " +
           "WHERE stat.admin = :admin))")
    Float totalEnergyConsumed(Timestamp dateFrom, Timestamp dateTo, Admin admin);
}