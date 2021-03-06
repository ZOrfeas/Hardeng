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
import Hardeng.Rest.services.SessionService

@SpringBootTest
@Transactional
class SessionSpec extends Specification{

    @Autowired
    private SessionService sessionService

    @Test
    def "Invalid startedOn String"(){
        when: "Expect illegal argument exception"
        sessionService.createSession("201807", "20180708", 19.8, "cash", 1, 1431, 1, 110)

        then:
        thrown(IllegalArgumentException)
    }
    @Test
    def "invalid finishedOn String"(){
        when: "Expect illegal argument exception"
        sessionService.createSession("20180708", "201807", 19.8, "cash", 1, 1431, 1, 110)

        then:
        thrown(IllegalArgumentException)
    }
    @Test
    def "Charging Point with 0 Id"(){
        when: "Expect Charging Point Not Found"
        sessionService.createSession("20180708", "20180708", 19.8, "cash", 0, 1431, 1, 110)

        then:
        thrown(ChargingPointNotFoundException)
    }
    @Test
    def "Price Policy with 0 Id"(){
        when: "Expect Price Policy Not Found"
        sessionService.createSession("20180708", "20180708", 19.8, "cash", 1, 0, 1, 110)

        then:
        thrown(PricePolicyNotFoundException)
    }
    @Test
    def "Car with 0 Id"(){
        when: "Expect Car Not Found"
        sessionService.createSession("20180708", "20180708", 19.8, "cash", 1, 1431, 0, 110)

        then:
        thrown(CarNotFoundException)
    }
    @Test
    def "Driver with 0 Id"(){
        when: "Expect Driver Not Found"
        sessionService.createSession("20180708", "20180708", 19.8, "cash", 1, 1431, 1, 0)

        then:
         thrown(DriverNotFoundException)
    }
    @Test
    def "Read Session with 0 Id"(){
        when: "Expect Session Not Found"
        sessionService.readSession(0)

        then:
        thrown(ChargingSessionNotFoundException)
    }
}