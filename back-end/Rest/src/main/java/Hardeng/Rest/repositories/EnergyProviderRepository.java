package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.EnergyProvider;

@Repository
public interface EnergyProviderRepository extends JpaRepository<EnergyProvider, Integer> {}
