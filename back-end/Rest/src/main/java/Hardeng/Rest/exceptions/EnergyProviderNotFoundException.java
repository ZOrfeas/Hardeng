package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class EnergyProviderNotFoundException extends RuntimeException{
    public EnergyProviderNotFoundException(Integer id) {
        super("Could not find Provider " + id);
    }
}
