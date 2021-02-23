package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class ChargingSessionNotFoundException extends RuntimeException{
    public ChargingSessionNotFoundException(Integer id) {
        super("Could not find charging session " + id);
    }
}