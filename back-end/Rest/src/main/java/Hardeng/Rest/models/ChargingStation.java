package Hardeng.Rest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer nrOfChargingPoints;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @ManyToOne
    private Admin admin;

    @ManyToOne
    private EnergyProvider eProvider;

    ChargingStation() {}
    ChargingStation(Integer nrOfChargingPoints, Double longitude, Double latitude, Admin admin, EnergyProvider eProvider) {
        this.nrOfChargingPoints = nrOfChargingPoints; this.longitude = longitude; this.latitude = latitude;
        this.admin = admin; this.eProvider = eProvider;
    }

    public Integer getId() {return this.id;}
    public Integer getNrOfChargingPoints() {return this.nrOfChargingPoints;}
    public Double getLongitude() {return this.longitude;}
    public Double getLatitude() {return this.latitude;}
    public Admin getAdmin() {return this.admin;}
    public EnergyProvider getEnergyProvider() {return this.eProvider;}

    public void setNrOfChargingPoints(Integer amount) {this.nrOfChargingPoints = amount;}
    public void setLatitude(Double newLatitude) {this.latitude = newLatitude;}
    public void setLongitude(Double newLongitude) {this.longitude = newLongitude;}
    public void setAdmin(Admin newAdmin) {this.admin = newAdmin;}
    public void setEnergyProvider(EnergyProvider newEProvider) {this.eProvider = newEProvider;}
}
