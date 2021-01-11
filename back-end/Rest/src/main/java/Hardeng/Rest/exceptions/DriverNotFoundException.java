package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(Integer driverId) {
        super("Could not find driver " + driverId);
    }
    public DriverNotFoundException(String username) {
        super("Could not find driver " + username);
    }
}