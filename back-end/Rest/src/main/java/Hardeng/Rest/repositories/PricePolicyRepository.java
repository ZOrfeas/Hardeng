package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Hardeng.Rest.models.PricePolicy;

public interface PricePolicyRepository extends JpaRepository<PricePolicy, Integer> {}