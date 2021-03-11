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
import Hardeng.Rest.services.ProviderService
import Hardeng.Rest.services.ProviderServiceImpl.SessProvObject

@SpringBootTest
@Transactional
class ProviderSpec extends Specification{

    @Autowired
    private ProviderService providerService


    @Test
    def "Invalid startedOn String"(){
        when: "Expect illegal argument exception"
        providerService.sessionsPerProvider(1,"201807", "20181009")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "invalid finishedOn String"(){
        when: "Expect illegal argument exception"
        providerService.sessionsPerProvider(1,"20180705", "201810")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Provider with id = 0"() {
        when: "should expect Energy Provider Not Found exception"
        providerService.sessionsPerProvider(0, "20180101", "20191231")

        then:
        def e = thrown(EnergyProviderNotFoundException)
        e.getMessage() == "Could not find Provider 0"
    }

    @Test
    def "No sessions obtained"() {
        when: "should expect No Data exception"
        providerService.sessionsPerProvider(1, "20190101", "20191231")

        then:
        thrown(NoDataException)
    }

    @Test
    def "Provider 43 from 01-11-2011 to 31-12-2019"() {
        given: 
        List<SessProvObject> res = providerService.sessionsPerProvider(43, "20111101", "20191231")

        when:
        def id = res[0].providerId
        def sessions = res.size()

        then:
        id == "43"
        sessions == 6
    }
}