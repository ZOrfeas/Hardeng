package Hardeng.Rest

import java.text.*
import java.util.*
import java.io.*
import java.nio.file.*

import spock.lang.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.transaction.annotation.Transactional
import org.junit.jupiter.api.Test

import Hardeng.Rest.exceptions.*
import Hardeng.Rest.repositories.*
import Hardeng.Rest.services.AdminService
import Hardeng.Rest.services.AdminServiceImpl
import Hardeng.Rest.services.AdminServiceImpl.AdminObject
import Hardeng.Rest.services.AdminServiceImpl.StatusObject
import Hardeng.Rest.services.AdminServiceImpl.UserObject
import Hardeng.Rest.services.AdminServiceImpl.SessionStatsObject

@SpringBootTest
@Transactional
class AdminSpec extends Specification {

    @Autowired
    private AdminService adminService

    @Test
    def "Successful isHealthy"() {
        given:
        
        Collection<Object> doAPingRes = Stub(Collection.class)
        doAPingRes.isEmpty() >> false
        AdminRepository adminRepo = Stub(AdminRepository.class)
        adminRepo.doAPing() >> doAPingRes
        AdminServiceImpl adminService = new AdminServiceImpl(adminRepo:adminRepo)

        when:
        StatusObject res = adminService.isHealthy()

        then:
        res.status == "OK"
    }

    @Test
    def "Unsuccessful isHealthy"() {
        given:
        Collection<Object> doAPingRes = Stub(Collection.class)
        doAPingRes.isEmpty() >> true
        AdminRepository adminRepo = Stub(AdminRepository.class)
        adminRepo.doAPing() >> doAPingRes
        AdminServiceImpl adminService = new AdminServiceImpl(adminRepo:adminRepo)

        when:
        StatusObject res = adminService.isHealthy()

        then:
        res.status == "failed"
    }

    @Test
    def "Successful resetSessions"() {
        when:
        StatusObject res = adminService.resetSessions()

        then:
        res.status == "OK"
    }

    
    @Test
    def "GetUsaerInfo of invalid Driver"() {
        when: "should expect Driver Not Found exception"
        adminService.getUserInfo("driver0")

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver driver0"
    }

    @Test
    def "GetUserInfo of driver1"() {
        when:
        UserObject user = adminService.getUserInfo("driver1")

        then:
        user.username == "driver1"
        user.driverName == "Libbie Shakeshaft"
        user.email == "driver1@hardeng.com"
        user.bonusPoints == 117
        user.carsOwnedList[0].id == 65
        user.carsOwnedList[1].id == 13
    }

    @Test
    def "UserMod by DRIVER"() {
        when: "should expect Bad Request exception"
        adminService.userMod("driver12", "passw12", "DRIVER", "Mommy Castel", "driver12@hd.com")

        then:
        thrown(BadRequestException)
    }

    @Test
    def "UserMod by STATION_ADMIN with missing driverName"() {
        when: "should expect Bad Request exception"
        adminService.userMod("driver12", "passw12", "STATION_ADMIN", null, "driver12@hd.com")

        then:
        thrown(BadRequestException)
    }

    @Test
    def "UserMod by STATION_ADMIN on existing driver"() {
        when:
        StatusObject res = adminService.userMod("driver1", "passw1", "STATION_ADMIN", "Mommy Castel", "driver1@hd.com")

        then:
        res.status == "Driver Successfully Updated"
    }

    @Test
    def "UserMod by STATION_ADMIN on new driver"() {
        when:
        StatusObject res = adminService.userMod("driver12", "passw12", "STATION_ADMIN", "Mommy Castel", "driver12@hd.com")

        then:
        res.status == "Successful Sign Up"
    }

    @Test
    def "UserMod by MASTER_ADMIN on existing admin"() {
        when:
        StatusObject res = adminService.userMod("user1", "passw1", "MASTER_ADMIN", "Mommy Castel", "admin1@hd.com")

        then:
        res.status == "Admin Successfully Updated"
    }

    @Test
    def "UserMod by MASTER_ADMIN on new admin"() {
        when:
        StatusObject res = adminService.userMod("admin12", "passw12", "MASTER_ADMIN", "Mommy Castel", "admin12@hd.com")

        then:
        res.status == "Successful Sign Up"
    }

    @Test
    def "SessionUpdate with invalid startedOn"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvStOn.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        thrown(IllegalArgumentException)

    }

    @Test
    def "SessionUpdate with invalid finishedOn"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvFinOn.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        thrown(IllegalArgumentException)

    }

    @Test
    def "SessionUpdate with invalid ChargingPoint"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvChP.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        def e = thrown(ChargingPointNotFoundException)
        e.getMessage() == "Could not find charging point 0"

    }

    @Test
    def "SessionUpdate with invalid PricePolicy"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvPrP.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        def e = thrown(PricePolicyNotFoundException)
        e.getMessage() == "Could not find price policy 535"

    }

    @Test
    def "SessionUpdate with invalid Driver"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvDr.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        def e = thrown(DriverNotFoundException)
        e.getMessage() == "Could not find driver 0"

    }

    @Test
    def "SessionUpdate with invalid Car"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvCar.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        def e = thrown(CarNotFoundException)
        e.getMessage() == "Could not find car 0"

    }

    @Test
    def "SessionUpdate with invalid CarDriver"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessionsInvCarDr.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        adminService.sessionUpdate(sessions)

        then:
        def e = thrown(CarDriverNotFoundException)
        e.getMessage() == "Could not find car 2 of driver 1"

    }

    @Test
    def "Successful SessionUpdate"() {
        given:
        Path path = Paths.get("src/test/resources/sessions/sessions.csv")
        byte[] content = Files.readAllBytes(path)
        MockMultipartFile sessions = new MockMultipartFile("sessions", "sessions.csv", "text/csv", content);

        when:
        SessionStatsObject res = adminService.sessionUpdate(sessions)

        then:
        res.sessionsInUploadedFile == 3
        res.sessionsImported == 3
        res.totalSessionsInDatabase == 23

    }


    @Test
    def "Read admin with id = 0"() {
        when: "should expect Admin Not Found exception"
        adminService.readAdmin(0)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Update admin with id = 0"() {
        when: "should expect Admin Not Found exception"
        adminService.updateAdmin(0, "admin1", "passw1", "admin0@hd.com", "Company 0", "Phone 0", "Location 0")

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Delete admin with id = 0"() {
        when: "should expect Admin Not Found exception"
        adminService.deleteAdmin(0)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "Delete existing admin"() {
        given: 
        adminService.deleteAdmin(1)

        when:
        adminService.deleteAdmin(1)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 1"
    }

    @Test
    def "CRU new admin"() {
        given: 
        AdminObject admin = adminService.createAdmin("admin0", "passw0", "admin0@hd.com", "Company 0", "Phone 0", "Location 0")
        admin = adminService.readAdmin(admin.adminId)
        adminService.updateAdmin(admin.adminId, "admin10", "passw0", "admin10@hd.com", "Company 10", "Phone 10", "Location 10")
        AdminObject updAdmin = adminService.readAdmin(admin.adminId)

        when:
        def username = admin.username
        def email = admin.email
        def companyName = admin.companyName
        def companyPhone = admin.companyPhone
        def companyLocation = admin.companyLocation

        def updUsername = updAdmin.username
        def updEmail = updAdmin.email
        def updCompanyName = updAdmin.companyName
        def updCompanyPhone = updAdmin.companyPhone
        def updCompanyLocation = updAdmin.companyLocation

        then:
        username == "admin0"
        email == "admin0@hd.com"
        companyName == "Company 0"
        companyPhone == "Phone 0"
        companyLocation == "Location 0"

        updUsername == "admin10"
        updEmail == "admin10@hd.com"
        updCompanyName == "Company 10"
        updCompanyPhone == "Phone 10"
        updCompanyLocation == "Location 10"
    }

    @Test
    def "Delete new admin"() {
        given:
        AdminObject admin = adminService.createAdmin("admin0", "passw0", "admin0@hd.com", "Company 0", "Phone 0", "Location 0")
        adminService.deleteAdmin(admin.adminId)
        

        when:
        adminService.readAdmin(admin.adminId)

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin " + admin.adminId.toString()
    }

    @Test
    def "Fetch id of invalid admin"() {
        when:
        adminService.fetchId("admin500")

        then:
        thrown(BadRequestException)
    }

    @Test
    def "GetTotalEnergy with invalid admin"() {
        when:
        adminService.getTotalEnergy(0, "20180101", "20191231")        

        then:
        def e = thrown(AdminNotFoundException)
        e.getMessage() == "Could not find admin 0"
    }

    @Test
    def "GetTotalEnergy with invalid dateFrom"() {
        when:
        adminService.getTotalEnergy(1, "201801", "20191231")        

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "GetTotalEnergy with invalid dateTo"() {
        when:
        adminService.getTotalEnergy(1, "20180101", "201911")       

        then:
        thrown(IllegalArgumentException)
    }
}