package Hardeng.Rest

import java.text.*
import java.util.*

import spock.lang.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Test

import Hardeng.Rest.exceptions.*
import Hardeng.Rest.services.PricePolicyService
import Hardeng.Rest.services.PricePolicyServiceImpl.PricePolicyObject
import Hardeng.Rest.services.PricePolicyServiceImpl.DriverPolicyObject

@SpringBootTest
@Transactional
class PricePolicySpec extends Specification {

    @Autowired
    private PricePolicyService policyService

    @Test
    def "Create price policy with invalid admin"() {
        when: "should expect Admin Not Found exception"
        policyService.createPricePolicy(100, 0.13, 0)
        
        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Read policy with id = 0"() {
        when: "should expect Price Policy Not Found exception"
        policyService.readPricePolicy(0)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0"
    }
    
    @Test
    def "Update policy with invalid admin"() {
        when: "should expect Admin Not Found exception"
        policyService.updatePricePolicy(1, 100, 0.13, 0)
        
        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Update policy with id = 0"() {
        when: "should expect Price Policy Not Found exception"
        policyService.updatePricePolicy(0, 100, 0.13, 1)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0"
    }
    
    @Test
    def "Delete policy with id = 0"() {
        when: "should expect Price Policy Not Found exception"
        policyService.deletePricePolicy(0)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0"
    }

    
    @Test
    def "CRU new price policy"() {
        given: 
        PricePolicyObject policy = policyService.createPricePolicy(100, 0.13, 1)
        policy = policyService.readPricePolicy(policy.pPolicyId)
        policyService.updatePricePolicy(policy.pPolicyId, 200, 0.15, 2)
        PricePolicyObject updPolicy = policyService.readPricePolicy(policy.pPolicyId)

        when:
        def kWh = policy.kWh
        def costPerKWh = policy.costPerKWh
        def adminId = policy.adminId

        def ukWh = updPolicy.kWh
        def uCostPerKWh = updPolicy.costPerKWh
        def uAdminId = updPolicy.adminId

        then:
        kWh == 100
        costPerKWh == (float) 0.13
        adminId == 1

        ukWh == 200
        uCostPerKWh == (float) 0.15
        uAdminId == 2
    }

    @Test
    def "Add invalid price policy to driver"() {
        when: "should expect Price Policy Not Found exception"
        policyService.addPricePolicyDriver(0, 1)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0" 
    }

    @Test
    def "Add invalid driver to price policy"() {
        when: "should expect Driver Not Found exception"
        policyService.addPricePolicyDriver(1, 0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0" 
    }

    @Test
    def "Add extra price policy to driver"() {
        given:
        DriverPolicyObject policies = policyService.getAllPricePolicyDriver(1)
        policyService.addPricePolicyDriver(1000, 1)
        DriverPolicyObject updPolicies = policyService.getAllPricePolicyDriver(1)
        
        when:
        def id = policies.driverID
        def size = policies.pricePolicies.size()

        def uId = updPolicies.driverID
        def uSize = updPolicies.pricePolicies.size()

        then:
        id == 1
        size == 5

        uId == 1
        uSize == 6
    }

    @Test
    def "Remove invalid price policy from driver"() {
        when: "should expect Price Policy Not Found exception"
        policyService.removePricePolicyDriver(0, 1)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0" 
    }

    @Test
    def "Remove invalid driver from price policy"() {
        when: "should expect Driver Not Found exception"
        policyService.removePricePolicyDriver(1, 0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0" 
    }

    @Test
    def "Remove price policy from driver"() {
        given:
        DriverPolicyObject policies = policyService.getAllPricePolicyDriver(1)
        policyService.removePricePolicyDriver(26, 1)
        DriverPolicyObject updPolicies = policyService.getAllPricePolicyDriver(1)
        
        when:
        def id = policies.driverID
        def size = policies.pricePolicies.size()

        def uId = updPolicies.driverID
        def uSize = updPolicies.pricePolicies.size()

        then:
        id == 1
        size == 5

        uId == 1
        uSize == 4
    }

    @Test
    def "Get policies of driver with id = 1"() {
        given:
        DriverPolicyObject res = policyService.getAllPricePolicyDriver(1)

        when:
        def id = res.driverID
        def size = res.pricePolicies.size()

        then:
        id == 1
        size == 5
    }

    @Test
    def "Get price policies of invalid driver"() {
        when: "should expect Driver Not Found exception"
        policyService.getAllPricePolicyDriver(0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0" 
    }
}