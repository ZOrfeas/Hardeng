package Hardeng.Rest

import spock.lang.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Test

import Hardeng.Rest.exceptions.*
import Hardeng.Rest.services.StationService
import Hardeng.Rest.services.StationServiceImpl.SessStationObject


@SpringBootTest
class StationSpec extends Specification {

    @Autowired
    private StationService stationService

    @Test
    @Transactional
    def "Station Service with id = 0"() {
        when: "should expect Charging Station Not Found exception"
        SessStationObject res = stationService.sessionsPerStation(0, "20180101", "20191231")

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }
}