package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class ChargingPointNotFoundException extends RuntimeException{
    public ChargingPointNotFoundException(Integer id) {
        super("Could not find charging point " + id);
    }
}
