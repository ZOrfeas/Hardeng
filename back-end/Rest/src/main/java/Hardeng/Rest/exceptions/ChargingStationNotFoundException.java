package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class ChargingStationNotFoundException extends RuntimeException{
    public ChargingStationNotFoundException(Integer id) {
        super("Could not find charging station " + id);
    }
}
