package Hardeng.Rest.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    @Query(value = "/* ping */ SELECT 1")
    Collection<Object> doAPing();

    Optional<Admin> findByUsername(String username);
}