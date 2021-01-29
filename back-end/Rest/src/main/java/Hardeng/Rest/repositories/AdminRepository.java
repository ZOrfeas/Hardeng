package Hardeng.Rest.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.Admin;

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


}