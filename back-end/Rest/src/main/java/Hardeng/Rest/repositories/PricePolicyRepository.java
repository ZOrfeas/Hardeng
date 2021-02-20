package Hardeng.Rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import Hardeng.Rest.models.Admin;
import Hardeng.Rest.models.PricePolicy;

@Repository
public interface PricePolicyRepository extends JpaRepository<PricePolicy, Integer> {

    /** 
     *  Returns Price Policy by admin {@code admin}
     * @param admin Admin in question
     * @return List of price policies
    */

    List<PricePolicy> findByAdmin(Admin admin);
}