package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.services.LoginService;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", consumes="application/x-www-form-urlencoded", produces = "application/json")
    public LoginObject login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        log.info("Login requested...");
        if (username == null || password == null) throw new BadRequestException();
        return loginService.login(username, password);
    }

}