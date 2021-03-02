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
import Hardeng.Rest.services.PointService
import Hardeng.Rest.services.PointServiceImpl.SessPointObject
import Hardeng.Rest.services.PointServiceImpl.PointObject

@SpringBootTest
@Transactional
class PointSpec extends Specification {

    @Autowired
    private PointService pointService

    @Test
    def "Point with id = 0"() {
        when: "should expect Charging Point Not Found exception"
        pointService.sessionsPerPoint(0, "20180101", "20191231")

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Invalid dateFrom string"() {
        when: "should expect Illegal argument exception"
        pointService.sessionsPerPoint(1, "201801", "20191231")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Invalid dateTo string"() {
        when: "should expect Illegal argument exception"
        pointService.sessionsPerPoint(1, "20180101", "201912")

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "Point with no charging sessions"() {
        when: "should expect No Data exception"
        pointService.sessionsPerPoint(4, "20100304", "20200802")

        then:
        thrown(NoDataException)
    }

    @Test
    def "Point 1 from 04-03-2010 to 02-08-2020"() {
        given: 
        SessPointObject res = pointService.sessionsPerPoint(1, "20100304", "20200802")

        when:
        def id = res.point
        def periodFrom = res.periodFrom
        def periodTo = res.periodTo
        def sessions = res.chargingSessionsList

        then:
        id == "1"
        periodFrom == "2010-03-04 00:00:00"
        periodTo == "2020-08-02 00:00:00"
        sessions.size() == 5
        sessions[0].sessionId == "1"
        sessions[1].sessionId == "2"
        sessions[2].sessionId == "5"
        sessions[3].sessionId == "12"
        sessions[4].sessionId == "13"
    }

    @Test
    def "Create point with invalid station"() {
        when: "should expect Charging Station Not Found exception"
        pointService.createPoint(0, 20, false, 1, 0)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Read point with id = 0"() {
        when: "should expect Charging Point Not Found exception"
        pointService.readPoint(0)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Update station with invalid charging station"() {
        when: "should expect Charging Station Not Found exception"
        pointService.updatePoint(1, 0, 20, false, 1, 0)

        then:
        def e = thrown(ChargingStationNotFoundException)
        e.getMessage() == "Could not find charging station 0"
    }

    @Test
    def "Update point with id = 0"() {
        when: "should expect Charging Point Not Found exception"
        pointService.updatePoint(0, 0, 20, false, 1, 1)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Delete point with id = 0"() {
        when: "should expect Charging Point Not Found exception"
        pointService.deletePoint(0)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"
    }

    @Test
    def "Delete existing station"() {
        given: 
        pointService.deletePoint(1)

        when:
        pointService.readPoint(1)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 1"
    }

    @Test
    def "CRU new charging point"() {
        given: 
        PointObject point = pointService.createPoint(0, 20, false, 1, 1)
        point = pointService.readPoint(point.pointId)
        pointService.updatePoint(point.pointId, 1, 25, true, 2, 2)
        PointObject updPoint = pointService.readPoint(point.pointId)

        when:
        def condition = point.currentCondition
        def maxEnergy = point.maxEnergyOutput
        def isOccupied = point.isOccupied
        def chargerType = point.chargerType
        def stationId = point.stationId

        def updCondition = updPoint.currentCondition
        def updMaxEnergy = updPoint.maxEnergyOutput
        def updIsOccupied = updPoint.isOccupied
        def updChargerType = updPoint.chargerType
        def updStationId = updPoint.stationId

        then:
        condition == 0
        maxEnergy == 20
        isOccupied == false
        chargerType == 1
        stationId == 1

        updCondition == 1
        updMaxEnergy == 25
        updIsOccupied == true
        updChargerType == 2
        updStationId == 2
    }

    @Test
    def "Delete new point"() {
        given:
        PointObject point = pointService.createPoint(0, 20, false, 1, 1)
        pointService.deletePoint(point.pointId)
        

        when:
        point = pointService.readPoint(point.pointId)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point " + point.pointId.toString()
    }
}