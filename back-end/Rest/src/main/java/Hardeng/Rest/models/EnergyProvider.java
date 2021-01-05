package Hardeng.Rest.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;

@Entity
public class EnergyProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false)
    private Double pricePerKwh;

    EnergyProvider() {}
    public EnergyProvider(String name, Double pricePerKwh) {
        this.name = name; this.pricePerKwh = pricePerKwh;
    }
    
    public Integer getId() {return this.id;}
    public String getName() {return this.name;}
    public Double getPricePerKwh() {return this.pricePerKwh;}

    public void setName(String newName) {this.name = newName;}
    public void setPricePerKwh(Double newPrice) {this.pricePerKwh = newPrice;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyProvider)) return false;
        EnergyProvider e = (EnergyProvider) o;
        return Objects.equals(this.id, e.id) && Objects.equals(this.name, e.name);
    }
    @Override
    public int hashCode() {return Objects.hash(this.id, this.name);}

    @Override
    public String toString() {return "Provider{" + "id=" + this.id + ", name='" + this.name + "'}";}
}
