package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.config.auth.SecurityConfig;
import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.LoginService;
import Hardeng.Rest.services.AdminServiceImpl.StatusObject;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login/{type}", consumes="application/x-www-form-urlencoded", produces = "application/json")
    public LoginObject login(@RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password, @PathVariable String type) {
        log.info("Login requested...");
        // remove this
        log.info(username+password+type);
        // remove this
        if (username == null || password == null) throw new BadRequestException();
        return loginService.login(username, password, type);
    }

    @PreAuthorize("hasRole('ROLE_" + SecurityConfig.masterAdminRole + "')")
    @GetMapping(value = "/testing")
    public StatusObject tester() {
        return new StatusObject("test up and running");
    }

}