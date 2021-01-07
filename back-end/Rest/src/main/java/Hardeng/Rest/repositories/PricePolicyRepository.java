package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.PricePolicy;

@Repository
public interface PricePolicyRepository extends JpaRepository<PricePolicy, Integer> {}