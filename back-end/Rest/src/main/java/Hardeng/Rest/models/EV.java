package Hardeng.Rest.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class EV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private String brandName;

    @Column(nullable = true)
    private Integer batteryCapacity;

    @Column(nullable = true)
    private String vehicleType;

    @ManyToOne
    private Driver driver;

    EV(){}
    
    EV(String brandName,Integer batteryCapacity,String vehicleType,Driver driver){
        this.brandName = brandName; this.batteryCapacity = batteryCapacity; 
        this.vehicleType = vehicleType; this.driver = driver;
    }
    
    public Integer getId() {return this.id;}
    public String getBrandName(){return this.brandName;}
    public Integer getBatteryCapacity(){return this.batteryCapacity;}
    public String getVehicleType(){return this.vehicleType;}
    public Driver getDriver(){return this.driver;}

    public void setBrandName(String newBrandName){this.brandName = newBrandName;}
    public void setBatteryCapacity(Integer newBatteryCapacity){this.batteryCapacity = newBatteryCapacity;}
    public void setVehicleType(String newVehicleType){this.vehicleType = newVehicleType;}
    public void setDriver(Driver newDriver){this.driver = newDriver;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EV)) return false;
        EV c = (EV) o;
        return Objects.equals(this.id, c.id);
    }
    @Override
    public int hashCode() {return Objects.hash(this.id);}
    @Override
    public String toString() {return "EV{" + "id=" + this.id + ",}";}

}
