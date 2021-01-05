package Hardeng.Rest.models;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    @Query(value = "/* ping */ SELECT 1", nativeQuery = true)
    Collection<Object> doAPing();

    Admin findByUsername(String username);
}