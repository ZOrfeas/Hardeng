package Hardeng.Rest.models;

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
    EnergyProvider(String name, Double pricePerKwh) {
        this.name = name; this.pricePerKwh = pricePerKwh;
    }
    
    public Integer getId() {return this.id;}
    public String getName() {return this.name;}
    public Double getPricePerKwh() {return this.pricePerKwh;}

    public void setName(String newName) {this.name = newName;}
    public void setPricePerKwh(Double newPrice) {this.pricePerKwh = newPrice;}
}
