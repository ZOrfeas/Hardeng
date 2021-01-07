package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.EV;

@Repository
public interface EVRepository extends JpaRepository<EV, Integer> {}
