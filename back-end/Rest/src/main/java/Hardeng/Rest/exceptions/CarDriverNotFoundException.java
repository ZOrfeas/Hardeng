package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class CarDriverNotFoundException extends RuntimeException{
    public CarDriverNotFoundException(Integer driverId, Integer carId) {
        super("Could not find car " + carId + " of driver " + driverId);
    }
}