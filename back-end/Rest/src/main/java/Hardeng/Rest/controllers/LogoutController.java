package Hardeng.Rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import Hardeng.Rest.services.LogoutService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LogoutController {
    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private LogoutService logoutService;

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-OBSERVATORY-AUTH") String token) {
        log.info("Logout requested...");
        return logoutService.logout(token);
    }

}