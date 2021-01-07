package Hardeng.Rest.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String brandName;

    @Column(nullable = false)
    private Integer batteryCapacity;

    @Column(nullable = false, length = 20)
    private String model;

    @Column(nullable = false)
    private Boolean acceptsType1;

    @Column(nullable = false)
    private Boolean acceptsType2;

    @Column(nullable = false)
    private Boolean acceptsType3;

    //Car driven by drivers
    @OneToMany(
        mappedBy = "car",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<CarDriver> carDrivers = new HashSet<>();

    Car(){}
    
    public Car(String brandName, Integer batteryCapacity, String model, Boolean accType1, Boolean accType2, Boolean accType3){
        this.brandName = brandName; this.batteryCapacity = batteryCapacity; this.model = model;
        this.acceptsType1 = accType1; this.acceptsType2 = accType2; this.acceptsType3 = accType3;
    }
    
    public Integer getId() {return this.id;}
    public String getBrandName(){return this.brandName;}
    public Integer getBatteryCapacity(){return this.batteryCapacity;}
    public String getModel(){return this.model;}
    public Boolean getacceptsType1(){return this.acceptsType1;}
    public Boolean getacceptsType2(){return this.acceptsType2;}
    public Boolean getacceptsType3(){return this.acceptsType3;}
    public Set<CarDriver> getDrivers() {return carDrivers;}

    public void setBrandName(String newBrandName){this.brandName = newBrandName;}
    public void setBatteryCapacity(Integer newBatteryCapacity){this.batteryCapacity = newBatteryCapacity;}
    public void setModel(String newModel){this.model = newModel;}
    public void setacceptsType1() {this.acceptsType1 = true;}
    public void resetacceptsType1() {this.acceptsType1 = false;}
    public void setacceptsType2() {this.acceptsType2 = true;}
    public void resetacceptsType2() {this.acceptsType2 = false;}
    public void setacceptsType3() {this.acceptsType3 = true;}
    public void resetacceptsType3() {this.acceptsType3 = false;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car c = (Car) o;
        return Objects.equals(this.id, c.id);
    }
    @Override
    public int hashCode() {return Objects.hash(this.id);}
    @Override
    public String toString() {return "Car{" + "id=" + this.id + ",}";}

}
