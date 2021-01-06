package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.EnergyProvider;

public interface EnergyProviderRepository extends JpaRepository<EnergyProvider, Integer> {}
