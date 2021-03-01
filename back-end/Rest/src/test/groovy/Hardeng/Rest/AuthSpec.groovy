package Hardeng.Rest

import spock.lang.Specification;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
// import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.authentication.AuthenticationManager;

import Hardeng.Rest.services.LoginService;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;
import Hardeng.Rest.config.auth.UserDetailsServiceImpl;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
import Hardeng.Rest.config.auth.SecurityConfig;
import Hardeng.Rest.config.auth.TokenUtil;
import Hardeng.Rest.exceptions.NotAuthorizedException;
import Hardeng.Rest.services.DriverService;
import Hardeng.Rest.services.AdminService;

@SpringBootTest
class AuthSpec extends Specification {

    @Autowired
    private LoginService logServ
    @Autowired
    private TokenUtil tokenUtil   
    @Autowired
    private DriverService dService
    @Autowired
    private AdminService aService

    // @Test
    // @Ignore
    // def "Stub test test"() {
    //     given: "condition"
    //     LoginService logServStub = Stub(LoginService.class)
    //     logServStub.login("test","pass","type") >> new LoginObject("true")
    //     when:
    //     def temp = logServStub.login("test","pass","type")
    //     then:
    //     temp.getToken() == "true";
    // }

    @Test
    def "Attempt login service interaction with created users"() {
        given: 'Stub UserDetailsServiceImpl returns fixed CustomUserPrincipal if invoked with arg "STATION_ADMIN|Adam", "MASTER_ADMIN|John", "DRIVER|Kostas"'
        dService.createDriver("Kwnstantinos Kwnstantios", "Kwsths", "dpass","email@email",0,12345L,54321L)
        aService.createAdmin("Adam", "apass", "mail@mail","AdamnSon.co","666","Hell")

        when: 'Login is attempted'
        def driverTokenObj = logServ.login("Kwsths","dpass","DRIVER")
        def adminTokenObj = logServ.login("Adam","apass","STATION_ADMIN")

        then: 'RetVal should be a correct token'
        tokenUtil.getUsernameFromToken(driverTokenObj.getToken()) == "DRIVER|Kwsths"
        tokenUtil.getUsernameFromToken(adminTokenObj.getToken()) == "STATION_ADMIN|Adam"
    }

    @Test
    def "Attemp login with temp users"() {
        when: 'Login is attempted'
        def driverTokenObj = logServ.login("driver1","driver1","DRIVER")
        def adminTokenObj = logServ.login("user1","passw1","STATION_ADMIN")

        then: 'RetVal should be a correct token'
        tokenUtil.getUsernameFromToken(driverTokenObj.getToken()) == "DRIVER|driver1"
        tokenUtil.getUsernameFromToken(adminTokenObj.getToken()) == "STATION_ADMIN|user1"

    }
}