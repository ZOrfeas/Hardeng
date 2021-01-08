package Hardeng.Rest.models;

import java.util.*;
import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.text.ParseException;

@Entity
public class ChargingSession {
    private final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @Column(nullable = false)
    private java.sql.Timestamp startedOn;

    @Column(nullable = false)
    private java.sql.Timestamp finishedOn;

    @Column(length = 10, nullable = false)
    private Integer energyDelivered;

    @Column(length = 10, nullable = false)
    private String payment;

    @ManyToOne
    private ChargingPoint chargingPoint;

    @ManyToOne
    private PricePolicy pricePolicy;
    
    
    @ManyToOne
    private CarDriver carDriver;

    ChargingSession() {}
    /**
     * ChargingSession entity constructor.
     * @param startedOn Τimestamp of the begining of the charging session
     * @param finishedOn Τimestamp of the ending of the charging session
     * @param energyDelivered Total energy offered
     * @param payment Method of payment
     * @param chargingPoint Charging point being used
     * @param pricePolicy Price policy selected by the driver
     * @param carDriver Electric vehicle being charged
     * Note: @param startedOn and @param finishedOn should be first parsed by {@link #parseTimestamp(String)}
     * e.x. ChargingSession(parseTimestamp("2020-05-01 12:30:00"), parseTimestamp("2020-05-01 13:30:00"), ...)
    */
    public ChargingSession(java.sql.Timestamp startedOn, java.sql.Timestamp finishedOn, Integer energyDelivered, String payment, ChargingPoint chargingPoint, PricePolicy pricePolicy, CarDriver carDriver) {
         this.startedOn = startedOn;
         this.finishedOn = finishedOn;
         this.energyDelivered = energyDelivered;
         this.payment = payment;
         this.carDriver = carDriver;
         this.chargingPoint = chargingPoint;
         this.pricePolicy = pricePolicy;
     }

    /* Getter methods */
    public Integer getSessionId () {
        return this.sessionId;
    }

    public java.sql.Timestamp getStartedOn () {
        return this.startedOn;
    }

    public java.sql.Timestamp getFinishedOn () {
        return this.finishedOn;
    }

    public Integer getEnergyDelivered () {
        return this.energyDelivered;
    }

    public String getPayment () {
        return this.payment;
    }

    public CarDriver getCarDriver () {
        return this.carDriver;
    }

    public ChargingPoint getChargingPoint () {
        return this.chargingPoint;
    }

    public PricePolicy getPricePolicy () {
        return this.pricePolicy;
    }

    /* Setter methods */
    public void setStartedOn (java.sql.Timestamp startedOn) {
        this.startedOn = startedOn;
    }

    public void setFinishedOn (java.sql.Timestamp finishedOn) {
        this.finishedOn = finishedOn;
    }

    public void setEnergyDelivered (Integer energyDelivered) {
        this.energyDelivered = energyDelivered;
    }

    public void setPayment (String payment) {
        this.payment = payment;
    }

    public void setCarDriver (CarDriver carDriver) {
        this.carDriver = carDriver;
    }

    public void setChargingPoint (ChargingPoint chargingPoint) {
        this.chargingPoint = chargingPoint;
    }

    public void setPricePolicy (PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    /* String to Timestap parser */
    private java.sql.Timestamp parseTimestamp(String timestamp) {
        try {
            return new java.sql.Timestamp(TIMESTAMP_FORMAT.parse(timestamp).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChargingSession)) return false;
        ChargingSession s = (ChargingSession) o;
        return Objects.equals(this.sessionId, s.sessionId) && Objects.equals(this.carDriver, s.carDriver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sessionId, this.carDriver);
    }

    @Override
    public String toString() {
        return "ChargingSession{" + "sessionId:" + this.sessionId + ", electricVehicle:\"" + this.carDriver.getCar() + "\"}";
    }
}