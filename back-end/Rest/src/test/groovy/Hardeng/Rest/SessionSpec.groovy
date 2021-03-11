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
        sessionService.createSession("201807", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 13, 1)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "invalid finishedOn String"(){
        when: "Expect illegal argument exception"
        sessionService.createSession("2018-10-08 16:01:34", "201807", 19.8, "cash", 1, 1, 13, 1)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Charging Point with 0 Id"(){
        when: "Expect Charging Point Not Found"
        sessionService.createSession("2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 0, 1, 13, 1)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Price Policy with 0 Id"(){
        when: "Expect Price Policy Not Found"
        sessionService.createSession("2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 0, 1, 110)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0"
    }

    @Test
    def "Car with 0 Id"(){
        when: "Expect Car Not Found"
        sessionService.createSession("2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 0, 1)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Driver with 0 Id"(){
        when: "Expect Driver Not Found"
        sessionService.createSession("2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 1, 0)

        then:
         def e = thrown(DriverNotFoundException)
         e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Invalid CarDriver"(){
        when: "Expect CarDriver Not Found"
        sessionService.createSession("2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 2, 1)

        then:
         def e = thrown(CarDriverNotFoundException)
         e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Read Session with 0 Id"(){
        when: "Expect Session Not Found"
        sessionService.readSession(0)

        then:
        def e = thrown(ChargingSessionNotFoundException)
        e.getMessage() == "Could not find charging session 0"
    }

    @Test
    def "Update Session with 0 Id"(){
        when: "Expect Session Not Found"
        sessionService.updateSession(0, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 13, 1)

        then:
        def e = thrown(ChargingSessionNotFoundException)
        e.getMessage() == "Could not find charging session 0"
    }

    @Test
    def "Update with invalid startedOn String"(){
        when: "Expect illegal argument exception"
        sessionService.updateSession(1, "201807", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 13, 1)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Update with invalid finishedOn String"(){
        when: "Expect illegal argument exception"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "201807", 19.8, "cash", 1, 1, 13, 1)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Update with charging Point with 0 Id"(){
        when: "Expect Charging Point Not Found"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 0, 1, 13, 1)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Update with price Policy with 0 Id"(){
        when: "Expect Price Policy Not Found"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 0, 1, 110)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 0"
    }

    @Test
    def "Update with car with 0 Id"(){
        when: "Expect Car Not Found"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 0, 1)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Update with driver with 0 Id"(){
        when: "Expect Driver Not Found"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 1, 0)

        then:
         def e = thrown(DriverNotFoundException)
         e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Update with invalid CarDriver"(){
        when: "Expect CarDriver Not Found"
        sessionService.updateSession(1, "2018-10-08 16:01:34", "2018-10-09 03:00:10", 19.8, "cash", 1, 1, 2, 1)

        then:
         def e = thrown(CarDriverNotFoundException)
         e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Delete Session with 0 Id"(){
        when: "Expect Session Not Found"
        sessionService.deleteSession(0)

        then:
        def e = thrown(ChargingSessionNotFoundException)
        e.getMessage() == "Could not find charging session 0"
    }

    @Test
    def "Initiate Session while all charging points occupied"(){
        when: "Expect Interval Server Error"
        sessionService.initiateSession(3)

        then:
        thrown(InternalServerErrorException)
    }

    @Test
    def "Initiate Session with invalid Station"(){
        when: "Expect Charging Station Not Found"
        sessionService.initiateSession(0)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Realease invalid point"(){
        when: "Expect Charging Point Not Found"
        sessionService.releasePoint(0)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Realease not occupied point"(){
        when: "Expect Internal Server Exception"
        sessionService.releasePoint(1)

        then:
        thrown(InternalServerErrorException)
    }
}