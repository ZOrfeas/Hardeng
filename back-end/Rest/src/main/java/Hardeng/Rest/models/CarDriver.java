package Hardeng.Rest.models;

import java.util.*;
import javax.persistence.*;

@Entity
public class CarDriver {

    @EmbeddedId
    private CarDriverId idi = new CarDriverId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("driverId")
    public Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carId")
    public Car car;

    @Column
    public Double currentCapacity;

    CarDriver() {}

    public CarDriver(Driver driver, Car car, Double currentCap) {
        this.driver = driver;
        this.car = car;
        this.currentCapacity = currentCap;
    }

    public Driver getDriver() {return this.driver;}
    public Car getCar() {return this.car;}
    public Double getCurrentCapacity() {return this.currentCapacity;}

    public void setDriver(Driver newDriver) {this.driver = newDriver;}
    public void setCar(Car newCar) {this.car = newCar;}
    public void setCurrentCapacity(Double newCap) {this.currentCapacity = newCap;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarDriver)) return false;
        CarDriver c = (CarDriver) o;
        return Objects.equals(this.driver, c.driver) && Objects.equals(this.car, c.car);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.driver, this.car);
    }
}