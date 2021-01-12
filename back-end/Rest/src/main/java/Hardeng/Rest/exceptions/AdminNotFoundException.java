package Hardeng.Rest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class AdminNotFoundException extends RuntimeException{
    public AdminNotFoundException(Integer adminId) {
        super("Could not find admin " + adminId);
    }
    public AdminNotFoundException(String username) {
        super("Could not find admin " + username);
    }
}