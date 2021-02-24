package Hardeng.Rest

import spock.lang.Specification
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import Hardeng.Rest.services.AdminServiceImpl.StatusObject
import Hardeng.Rest.services.AdminService

@SpringBootTest
class Trial extends Specification {


    @Autowired
    private AdminService adServ

    @Test
    def "there should be database access"() {
        expect: "Should be OK"
        adServ.isHealthy().getStatus() == "OK"
    }
}

