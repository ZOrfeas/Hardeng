package Hardeng.Rest.exceptions;

@SuppressWarnings("serial")
public class ChargingStationNotFoundException extends RuntimeException{
    public ChargingStationNotFoundException(Integer id) {
        super("Could not find charging station " + id);
    }
}
