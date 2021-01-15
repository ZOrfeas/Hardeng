package Hardeng.Rest.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    public static String masterAdminRole = "MASTER_ADMIN";
    public static String stationAdminRole = "STATION_ADMIN";
    public static String driverRole = "DRIVER";

    public static SimpleGrantedAuthority masterAuthority = new SimpleGrantedAuthority("ROLE_" + masterAdminRole);
    public static SimpleGrantedAuthority stationAdminAuthority = new SimpleGrantedAuthority("ROLE_" + stationAdminRole);
    public static SimpleGrantedAuthority driverAuthority = new SimpleGrantedAuthority("ROLE_" + driverRole);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            csrf().disable().
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().
            authorizeRequests().
            antMatchers(/** fill me */).permitAll(). // here go whatever endpoints are deemed to be accessible by all
            antMatchers(/** fill me */).hasRole(masterAdminRole); // here go endpoints only we have access to
        
    }

    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
