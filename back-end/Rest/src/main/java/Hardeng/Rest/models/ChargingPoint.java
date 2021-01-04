package Hardeng.Rest.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.geo.Point;


@Entity
public class ChargingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @ManyToOne
    private ChargingStation cStation;

    @Column(nullable = false)
    private Integer maxEnergyOutput;

    @Column(nullable = false)
    private Boolean isOccupied;
    
    @Column(nullable = false, length = 8)
    private Integer condition; 
    
    public enum Condition {
        Operational(0), Down(-1), UnderRepairs(1), NeedsCheck(2), UnderConstruction(3);
        public final Integer value;
        private static final Map<Integer, Condition> BY_LABEL = new HashMap<>();
        private Condition(Integer value) {this.value = value;}
        /** This runs once and populates the lookup table from Integer to Condition */
        static {for (Condition c: values()) {BY_LABEL.put(c.value, c);}}
        /** Translates code to Condition type 
         * @param code The value to be translated 
         * @return The corresponding Condition 
         */
        @Override
        public String toString() {return this.name();}
        public static Condition meaningOfCode(Integer code) {return BY_LABEL.get(code);}
    }

    ChargingPoint() {}
    /**
     * Point entity constructor.
     * @param longitude self-explanatory
     * @param latitude self-explanatory
     * @param operatorId The Id of the Corresponding Point operator
     * @param maxOutput The max power output this point is capable of
     * @param condition The current operational condition/status of the point
     * @param isOccupied Stores whether it's being used at the moment.
     */
    ChargingPoint(Double longitude, Double latitude, ChargingStation cStation , Integer maxOutput, Integer condition,  Boolean isOccupied) {
        this.longitude = longitude; this.latitude = latitude; this.cStation = cStation; this.maxEnergyOutput = maxOutput;
        this.condition = condition; this.isOccupied = isOccupied;
    }
    /** 
     * Overload of the {@link #Point(Double, Double, Integer, Integer, Integer, Boolean)} 
     * method with default {@code condition=0}(OPERATIONAL)
     * and {@code isOccupied=false}
     */
    ChargingPoint(Double longitude, Double latitude, ChargingStation cStation, Integer maxOutput) {
        this.longitude = longitude; this.latitude = latitude; this.cStation = cStation; this.maxEnergyOutput = maxOutput;
        this.condition = 0; this.isOccupied = false;
    }


    public Integer getId() {return this.id;}
    public Double getLatitude() {return this.latitude;}
    public Double getLongitude() {return this.longitude;}
    public Integer getMaxOutput() {return this.maxEnergyOutput;}
    public Boolean isOccupied() {return this.isOccupied;}
    public Condition getCondition() {return Condition.meaningOfCode(this.condition);}
    public ChargingStation getCStation() {return this.cStation;}
    /** @return Point where X is latitude and Y is longitude */
    public Point getCoordinates() {return (new Point(this.latitude, this.longitude));}

    public void setLatitude(Double newLatitude) {this.latitude = newLatitude;}
    public void setLongitude(Double newLongitude) {this.longitude = newLongitude;}
    public void setMaxOutput(Integer newMaxOutput) {this.maxEnergyOutput = newMaxOutput;}
    public void setIsOccupied() {this.isOccupied = true;}
    public void resetIsOccupied() {this.isOccupied = false;}
    public void setCondition(Condition status) {this.condition = status.value;}
    public void setCStation(ChargingStation cStation) {this.cStation = cStation;}
    /** @param newCoordinates contains latitude as its X(first) value and longitude as its Y(second) value */
    public void setCoordinates(Point newCoordinates) {this.latitude = newCoordinates.getX(); this.longitude = newCoordinates.getY();}
}
