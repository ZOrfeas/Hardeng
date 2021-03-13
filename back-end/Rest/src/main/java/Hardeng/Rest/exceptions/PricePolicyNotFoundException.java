package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class PricePolicyNotFoundException extends RuntimeException{
    public PricePolicyNotFoundException(Integer id) {
        super("Could not find price policy " + id);
    }
}
