package Hardeng.Rest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class PricePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer kWh;

    @Column(nullable = false)
    private Integer costPerKWh;

    //mappedBy references the pricePolicies, which we define with the corresponding JoinTable
    @ManyToMany(mappedBy = "pricePolicies", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Driver> drivers = new HashSet<>();

    PricePolicy(){}

    public PricePolicy(Integer kWh, Integer costPerKWh){
        this.kWh = kWh; this.costPerKWh = costPerKWh;
    }

    public Integer getID(){return this.id;}
    public Integer getKWh(){return this.kWh;}
    public Integer getCostPerKWh(){return this.costPerKWh;}
    public Set<Driver> getDrivers(){return drivers;}

    public void setkWh(Integer newKWh){this.kWh = newKWh;}
    public void setCostPerKWh(Integer newCostPerKWh){this.costPerKWh = newCostPerKWh;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PricePolicy)) return false;
        PricePolicy u = (PricePolicy) o;
        return Objects.equals(this.id, u.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "PricePolicy{" + "id=" + this.id + ", kWh=" + this.kWh + ", costPerKWh=" + this.costPerKWh+ "}";
    }

}
