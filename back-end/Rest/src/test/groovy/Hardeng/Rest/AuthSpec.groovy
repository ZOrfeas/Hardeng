package Hardeng.Rest

import spock.lang.Specification;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.spockframework.spring.SpringBean
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

@SpringBootTest
// @Transactional
class AuthSpec extends Specification {

    @Autowired
    private LoginService logServ;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenUtil tokenUtil;    

    // @SpringBean
    // private UserDetailsServiceImpl udServStub = Stub();
    // @SpringBean 
    // private LoginService logServStub = Stub(); 
    // def setup() {
    //     impl = Stub(LoginServiceImpl)

    // }

    // functional showcase of Stubbing
    @Test
    def "Stub test test"() {
        given: "condition"
        LoginService logServStub = Stub(LoginService.class)
        logServStub.login("test","pass","type") >> new LoginObject("true")

        when:
        def temp = logServStub.login("test","pass","type")

        then:
        temp.getToken() == "true";
    }

    @Test
    // note to self,  using Mocks to count times accessed will probably be helpful
    def "Attempt login service interaction"() {
        given: 'Stub UserDetailsServiceImpl returns fixed CustomUserPrincipal if invoked with arg "STATION_ADMIN|Adam", "MASTER_ADMIN|John", "DRIVER|Kostas"'
        UserDetailsServiceImpl udServStub = Stub(UserDetailsServiceImpl.class)
        udServStub.loadUserByUsername("STATION_ADMIN|Adam") >> new CustomUserPrincipal("Adam","STATION_ADMIN",encoder.encode("12345"),SecurityConfig.stationAdminAuthorities)
        udServStub.loadUserByUsername("MASTER_ADMIN|John") >> new CustomUserPrincipal("John","MASTER_ADMIN",encoder.encode("strong_password"),SecurityConfig.masterAuthorities)
        udServStub.loadUserByUsername("DRIVER|Kostas") >> new CustomUserPrincipal("Kostas","DRIVER",encoder.encode("kwnstantinos"),SecurityConfig.driverAuthorities)

        when: 'Login is attempted'
        def driverTokenObj = logServ.login("Kostas","kwnstantinos","DRIVER")
        def adminTokenObj = logServ.login("Adam","12345","STATION_ADMIN")
        def masterTokenObj = logServ.login("John","strong_password","MASTER_ADMIN")

        then: 'RetVal should be a correct token'
        tokenUtil.getUsernameFromToken(driverToken.getToken()) == "DRIVER|Kostas"
        tokenUtil.getUsernameFromToken(adminToken.getToken()) == "STATION_ADMIN|Adam"
        tokenUtil.getUsernameFromToken(masterToken.getToken()) == "MASTER_ADMIN|John"
    }
}