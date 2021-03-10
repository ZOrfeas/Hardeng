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
import Hardeng.Rest.services.DriverService
import Hardeng.Rest.services.DriverServiceImpl.DriverObject

@SpringBootTest
@Transactional
class DriverSpec extends Specification {

    @Autowired
    private DriverService driverService

    @Test
    def "Read driver with id = 0"() {
        when: "should expect Driver Not Found exception"
        driverService.readDriver(0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Update driver with id = 0"() {
        when: "should expect Driver Not Found exception"
        driverService.updateDriver(0, "driver driver", "driver0", "passw0", "driver0@hardeng.com", 10, 4041591182590, 2041591182597)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Delete driver with id = 0"() {
        when: "should expect Driver Not Found exception"
        driverService.deleteDriver(0)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"
    }

    @Test
    def "Delete existing driver"() {
        given: 
        driverService.deleteDriver(1)

        when:
        driverService.deleteDriver(1)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 1"
    }

    @Test
    def "CRU new driver"() {
        given: 
        DriverObject driver = driverService.createDriver("driver driver", "driver", "passw", "driver@hardeng.com", 10, 4041591182590, 2041591182597)
        driver = driverService.readDriver(driver.driverId)
        driverService.updateDriver(driver.driverId, "new driver", "drivern", "passw", "drivern@hardeng.com", 120, 4041591182600, 2041591182598)
        DriverObject updDriver = driverService.readDriver(driver.driverId)

        when:
        def name = driver.driverName
        def username = driver.username
        def email = driver.email
        def bonusPoint = driver.bonusPoints
        def card = driver.cardId
        def wallet = driver.walletId

        def updName = updDriver.driverName
        def updUsername = updDriver.username
        def updEmail = updDriver.email
        def updBonusPoint = updDriver.bonusPoints
        def updCard = updDriver.cardId
        def updWallet = updDriver.walletId

        then:
        name == "driver driver"
        username == "driver"
        email == "driver@hardeng.com"
        bonusPoint == 10
        card == 4041591182590
        wallet == 2041591182597

        updName == "new driver"
        updUsername == "drivern"
        updEmail == "drivern@hardeng.com"
        updBonusPoint == 120
        updCard == 4041591182600
        updWallet == 2041591182598
    }

    @Test
    def "Delete new driver"() {
        given:
        DriverObject driver = driverService.createDriver("driver driver", "driver", "passw", "driver@hardeng.com", 10, 4041591182590, 2041591182597)
        driverService.deleteDriver(driver.driverId)
        

        when:
        driverService.readDriver(driver.driverId)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver " + driver.driverId.toString()
    }

    @Test
    def "Fetch id of invalid driver"() {
        when:
        driverService.fetchId("driver500")

        then:
        thrown(BadRequestException)
    }
}