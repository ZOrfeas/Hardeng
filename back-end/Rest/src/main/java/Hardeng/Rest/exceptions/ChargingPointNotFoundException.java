package Hardeng.Rest.exceptions;

@SuppressWarnings("serial")
public class ChargingPointNotFoundException extends RuntimeException{
    public ChargingPointNotFoundException(Integer id) {
        super("Could not find charging point " + id);
    }
}
