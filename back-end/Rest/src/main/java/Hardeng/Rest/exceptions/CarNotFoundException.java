package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(Integer carId) {
        super("Could not find car " + carId);
    }
}