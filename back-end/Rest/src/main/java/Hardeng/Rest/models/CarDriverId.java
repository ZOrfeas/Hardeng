package Hardeng.Rest.models;

import java.util.*;
import javax.persistence.*;
import java.io.Serializable;

/** Embedable congregation object necessary for manual ManyToOne between Driver and Car models */
@Embeddable
@SuppressWarnings("serial")
public class CarDriverId implements Serializable {

    @Column
    private Integer driverId;

    @Column
    private Integer carId;

    CarDriverId() {}

    public CarDriverId(Integer driverId, Integer carId) {
        this.driverId = driverId;
        this.carId = carId;
    }

    public Integer getDriverId() {return this.driverId;}
    public Integer getCarId() {return this.carId;}

    public void setDriverId(Integer newDriverId) {this.driverId = newDriverId;}
    public void setCarId(Integer newCarId) {this.carId = newCarId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarDriverId)) return false;
        CarDriverId c = (CarDriverId) o;
        return Objects.equals(this.driverId, c.driverId) && Objects.equals(this.carId, c.carId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.driverId, this.carId);
    }
}