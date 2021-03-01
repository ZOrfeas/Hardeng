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
import Hardeng.Rest.services.StationService
import Hardeng.Rest.services.StationServiceImpl.SessStationObject
import Hardeng.Rest.services.StationServiceImpl.StationObject
import Hardeng.Rest.services.StationServiceImpl.NearbyStationObject

@SpringBootTest
@Transactional
class StationSpec extends Specification {

    @Autowired
    private StationService stationService

    @Test
    def "Station with id = 0"() {
        when: "should expect Charging Station Not Found exception"
        stationService.sessionsPerStation(0, "20180101", "20191231")

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Invalid dateFrom string"() {
        when: "should expect Illegal argument exception"
        stationService.sessionsPerStation(1, "201801", "20191231")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Invalid dateTo string"() {
        when: "should expect Illegal argument exception"
        stationService.sessionsPerStation(1, "20180101", "201912")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Station with no charging points"() {
        given: 
        StationObject station = stationService.createStation(37.873806, 23.759401, "Venezouelas 1", 2, 43)

        when: "should expect No Data exception"
        stationService.sessionsPerStation(station.stationId, "20180101", "20191231")

        then:
        thrown(NoDataException)
    }

    @Test
    // επίσης μάλλον μη ουσιώδες
    def "Station 200 from 01-11-2011 to 31-12-2019"() {
        given: 
        SessStationObject res = stationService.sessionsPerStation(200, "20111101", "20191231")

        when:
        def id = res.stationId
        def periodFrom = res.periodFrom
        def periodTo = res.periodTo
        def points = res.sessionsSummaryList.size()

        then:
        id == "200"
        periodFrom == "2011-11-01 00:00:00"
        periodTo == "2019-12-31 00:00:00"
        points == 1
    }

    @Test
    def "Nearby stations given valid input"() {
        given: 
        List<NearbyStationObject> res = stationService.nearbyStations(37.924371, -75.354259, 0.25)

        when:
        def size = res.size()
        def label1 = res[0].label

        then:
        size == 1
        label1 == "7058 Maddox Blvd"
    }

    @Test
    def "Nearby stations given small radius"() {
        when: "should expect No Data exception" 
        stationService.nearbyStations(37.9838, 23.7275, 0.0001)

        then:
        thrown(NoDataException)
    }

    @Test
    // μάλλον χρειάζεται κατάλληλη προσαρμογή
    def "Nearby stations given large radius"() {
        given:
        List<NearbyStationObject> res = stationService.nearbyStations(37.9838, 23.7275, 10)

        when:
        def size = res.size()

        then:
        size == 285
    }

    @Test
    def "Create station with invalid admin"() {
        when: "should expect Admin Not Found exception"
        stationService.createStation(37.873806, 23.759401, "Venezouelas 1", 0, 2)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Create station with invalid energy provider"() {
        when: "should expect Energy Provider Not Found exception"
        stationService.createStation(37.873806, 23.759401, "Venezouelas 1", 2, 0)

        then:
        def e = thrown(EnergyProviderNotFoundException)
        e.getMessage() == "Could not find Provider 0"
    }

    @Test
    def "Read station with id = 0"() {
        when: "should expect Charging Station Not Found exception"
        stationService.readStation(0)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Update station with invalid admin"() {
        when: "should expect Admin Not Found exception"
        stationService.updateStation(27050, 37.873806, 23.759401, "Venezouelas 1", 0, 2)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Update station with invalid energy provider"() {
        when: "should expect Energy Provider Not Found exception"
        stationService.updateStation(27050, 037.873806, 23.759401, "Venezouelas 1", 2, 0)

        then:
        def e = thrown(EnergyProviderNotFoundException)
        e.getMessage() == "Could not find Provider 0"
    }

    @Test
    def "Update station with id = 0"() {
        when: "should expect Charging Station Not Found exception"
        stationService.updateStation(0, 037.873806, 23.759401, "Venezouelas 1", 2, 2)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Delete station with id = 0"() {
        when: "should expect Charging Station Not Found exception"
        stationService.deleteStation(0)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Delete existing station"() {
        given: 
        stationService.deleteStation(27050)

        when:
        stationService.readStation(27050)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 27050"
    }

    @Test
    def "CRU new charging station"() {
        given: 
        StationObject station = stationService.createStation(37.873806, 23.759401, "Venezouelas 1", 2, 43)
        station = stationService.readStation(station.stationId)
        stationService.updateStation(station.stationId, 37.873806, 23.759401, "Venezouelas 2", 2, 43)
        StationObject updStation = stationService.readStation(station.stationId)

        when:
        def points = station.nrOfChargingPoints
        def lon = station.longitude
        def lat = station.latitude
        def address = station.addressLine

        def updAddress = updStation.addressLine

        then:
        points == 0
        lon == 23.759401
        lat == 37.873806
        address == "Venezouelas 1"

        updAddress == "Venezouelas 2"
    }

    @Test
    def "Delete new station"() {
        given:
        StationObject station = stationService.createStation(37.873806, 23.759401, "Venezouelas 1", 2, 43)
        stationService.deleteStation(station.stationId)
        

        when:
        stationService.readStation(station.stationId)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station " + station.stationId.toString()
    }
}