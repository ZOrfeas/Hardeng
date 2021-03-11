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
import Hardeng.Rest.services.EVService
import Hardeng.Rest.services.EVServiceImpl.SessEVObject
import Hardeng.Rest.services.EVServiceImpl.EVObject
import Hardeng.Rest.services.EVServiceImpl.DriverCarObject

@SpringBootTest
@Transactional
class EVSpec extends Specification {

    @Autowired
    private EVService evService

    @Test
    def "Invalid dateFrom string"() {
        when: "should expect Illegal argument exception"
        evService.sessionsPerEV(1, 13, "201801", "20191231")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Invalid dateTo string"() {
        when: "should expect Illegal argument exception"
        evService.sessionsPerEV(1, 13, "20180101", "201912")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Driver with id = 0"() {
        when: "should expect Driver Not Found exception"
        evService.sessionsPerEV(0, 1, "20180101", "20191231")

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Car with id = 0"() {
        when: "should expect Car Not Found exception"
        evService.sessionsPerEV(1, 0, "20180101", "20191231")

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Invalid EV"() {
        when: "should expect Car Not Found exception"
        evService.sessionsPerEV(1, 2, "20180101", "20191231")

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "EV with no charging sessions"() {
        when: "should expect No Data exception"
        evService.sessionsPerEV(1, 65, "20180101", "20191231")

        then:
        thrown(NoDataException)
    }

    @Test
    def "Car 13 of driver 1 from 04-03-2010 to 02-08-2020"() {
        given: 
        SessEVObject res = evService.sessionsPerEV(1, 13, "20100304", "20200802")

        when:
        def id = res.vehicleID
        def periodFrom = res.periodFrom
        def periodTo = res.periodTo
        def totalEnergy = res.totalEnergyConsumed
        def points = res.nrOfVisitedPoints
        def sessions = res.nrOfVehicleChargingSessions
        def sessionsList = res.sessionsSummaryList.size()

        then:
        id == "1-13"
        periodFrom == "2010-03-04 00:00:00"
        periodTo == "2020-08-02 00:00:00"
        totalEnergy == (float) 7585.2275
        points == 3
        sessions == 14
        sessionsList == 14
    }

    @Test
    def "Create EV with invalid driver"() {
        when: "should expect Driver Not Found exception"
        evService.createEV(0, 1, 20)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Create EV with invalid car"() {
        when: "should expect Car Not Found exception"
        evService.createEV(1, 0, 20)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Read EV with invalid driver"() {
        when: "should expect Driver Not Found exception"
        evService.readEV(0, 1)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Read EV with invalid car"() {
        when: "should expect Car Not Found exception"
        evService.readEV(1, 0)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    
    @Test
    def "Read invalid EV"() {
        when: "should expect Car Driver Not Found exception"
        evService.readEV(1, 2)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Update EV with invalid driver"() {
        when: "should expect Driver Not Found exception"
        evService.updateEV(0, 1, 20)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Update EV with invalid car"() {
        when: "should expect Car Not Found exception"
        evService.updateEV(1, 0, 20)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Update invalid EV"() {
        when: "should expect Car Not Found exception"
        evService.updateEV(1, 2, 20)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Delete EV with invalid driver"() {
        when: "should expect Driver Not Found exception"
        evService.deleteEV(0, 1)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Delete EV with invalid car"() {
        when: "should expect Car Not Found exception"
        evService.deleteEV(1, 0)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"
    }

    @Test
    def "Delete invalid EV"() {
        when: "should expect Car Not Found exception"
        evService.deleteEV(1, 2)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Delete existing EV"() {
        given: 
        evService.deleteEV(1, 13)

        when:
        evService.deleteEV(1, 13)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 13 of driver 1"
    }

    @Test
    def "CRU new EV"() {
        given: 
        EVObject ev = evService.createEV(1, 2, 20)
        ev = evService.readEV(1, 2)
        evService.createEV(2, 3, 15)
        EVObject updEv = evService.readEV(2, 3)

        when:
        def driver = ev.driverId
        def car = ev.carId
        def capacity = ev.currentCap

        def updDriver = updEv.driverId
        def updCar = updEv.carId
        def updCapacity = updEv.currentCap

        then:
        driver == 1
        car == 2
        capacity == 20

        updDriver == 2
        updCar == 3
        updCapacity == 15
    }

    @Test
    def "Delete new EV"() {
        given:
        EVObject ev = evService.createEV(1, 2, 20)
        evService.deleteEV(1, 2)
        

        when:
        evService.readEV(1, 2)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"
    }

    @Test
    def "Get all cars of invalid driver"() {
        when: "should expect Driver Not Found exception"
        evService.getAllCarDriver(0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Get all cars of driver 1"() {
        given:
        DriverCarObject res = evService.getAllCarDriver(1)

        when:
        def id = res.driverId
        def cars = res.cars.size()
        def car1Id = res.cars[0].carId
        def car2Id = res.cars[1].carId 

        then:
        id == 1
        cars == 2
        car1Id == 65
        car2Id == 13
    }
}