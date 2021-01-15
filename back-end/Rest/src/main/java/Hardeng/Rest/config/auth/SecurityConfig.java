package Hardeng.Rest.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private static String backendAdminRole = "BACKEND_ADMIN";
    private static String frontendConsumerRole = "FRONTEND";
    private static String stationAdminRole = "STATION_ROLE";
    private static String driverRole = "DRIVER_ROLE";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            csrf().disable().
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().
            authorizeRequests().
            antMatchers(/** fill me */).permitAll(). // here go whatever endpoints are deemed to be accessible by all
            antMatchers(/** fill me */).hasRole(backendAdminRole); // here go endpoints only we have access to
            
    }

    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
