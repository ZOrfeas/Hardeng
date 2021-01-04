package Hardeng.Rest.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
// import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


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
    private Integer operatorId;

    @Column(nullable = false)
    private Integer maxOutput;

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
    ChargingPoint(Double longitude, Double latitude, Integer operatorId , Integer maxOutput, Integer condition,  Boolean isOccupied) {
        this.longitude = longitude; this.latitude = latitude; this.operatorId = operatorId; this.maxOutput = maxOutput;
        this.condition = condition; this.isOccupied = isOccupied;
    }
    /** 
     * Overload of the {@link #Point(Double, Double, Integer, Integer, Integer, Boolean)} method with default {@code condition=0}(OPERATIONAL)
     * and {@code isOccupied=false}
     */
    ChargingPoint(Double longitude, Double latitude, Integer operatorId, Integer maxOutput) {
        this.longitude = longitude; this.latitude = latitude; this.operatorId = operatorId; this.maxOutput = maxOutput;
        this.condition = 0; this.isOccupied = false;
    }


    public Integer getId() {return this.id;}
    public Double getLatitude() {return this.latitude;}
    public Double getLongitude() {return this.longitude;}
    public Integer getMaxOutput() {return this.maxOutput;}
    public Boolean isOccupied() {return this.isOccupied;}
    public Condition getCondition() {return Condition.meaningOfCode(this.condition);}

    public void setLatitude(Double newLatitude) {this.latitude = newLatitude;}
    public void setLongitude(Double newLongitude) {this.longitude = newLongitude;}
    public void setMaxOutput(Integer newMaxOutput) {this.maxOutput = newMaxOutput;}
    public void setIsOccupied() {this.isOccupied = true;}
    public void resetIsOccupied() {this.isOccupied = false;}
    public void setCondition(Condition status) {this.condition = status.value;}
}
