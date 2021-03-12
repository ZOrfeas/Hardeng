package Hardeng.Rest.config.auth;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Security configuration class */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    public final static String masterAdminRole = "MASTER_ADMIN";
    public final static String stationAdminRole = "STATION_ADMIN";
    public final static String driverRole = "DRIVER";

    public final static SimpleGrantedAuthority masterAuthority = new SimpleGrantedAuthority("ROLE_" + masterAdminRole);
    public final static SimpleGrantedAuthority stationAdminAuthority = new SimpleGrantedAuthority("ROLE_" + stationAdminRole);
    public final static SimpleGrantedAuthority driverAuthority = new SimpleGrantedAuthority("ROLE_" + driverRole);

    public final static List<SimpleGrantedAuthority> driverAuthorities = 
        new ArrayList<>(List.of(driverAuthority));
    public final static List<SimpleGrantedAuthority> stationAdminAuthorities =
        new ArrayList<>(List.of(driverAuthority, stationAdminAuthority));
    public final static List<SimpleGrantedAuthority> masterAuthorities =
        new ArrayList<>(List.of(driverAuthority, stationAdminAuthority, masterAuthority));
    
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthEntryPoint customEntryPoint;

    @Autowired
    private CustomFilter customFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }    
    
    /** Endpoint access configurer */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
            csrf().disable().
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
            and().
            exceptionHandling().authenticationEntryPoint(customEntryPoint).
            and().
            authorizeRequests().
            antMatchers("/login/**").permitAll(). // here go whatever endpoints are deemed to be accessible by all
            antMatchers("/frontend").hasRole(masterAdminRole).
            anyRequest().authenticated(); // here go endpoints only we have access to
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
