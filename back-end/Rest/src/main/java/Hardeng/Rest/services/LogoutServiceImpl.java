package Hardeng.Rest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;



@Service
public class LogoutServiceImpl implements LogoutService {
    private static final Logger log = LoggerFactory.getLogger(LogoutServiceImpl.class);

    @Override
    public ResponseEntity<Void> logout(String token) {
        //Add logout logic
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

