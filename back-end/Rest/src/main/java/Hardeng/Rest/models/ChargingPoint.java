package Hardeng.Rest.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.geo.Point;


@Entity
public class ChargingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 8)
    private Integer currentCondition; 

    @Column(nullable = false)
    private Integer maxEnergyOutput;

    @Column(nullable = false)
    private Boolean isOccupied;

    @Column(nullable = false)
    private Integer chargerType;

    @ManyToOne
    private ChargingStation cStation;
    
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
     * @param condition The current operational condition/status of the point
     * @param maxOutput The max power output this point is capable of
     * @param isOccupied Stores whether it's being used at the moment.
     * @param chargerType The type of charger offered (1,2 or 3)
     * @param cStation The corresponding charging station containing this charging point
     */
    public ChargingPoint(Integer condition, Integer maxOutput, Boolean isOccupied, Integer chargerType, ChargingStation cStation) {
        this.currentCondition = condition; this.maxEnergyOutput = maxOutput;
        this.isOccupied = isOccupied; this.chargerType = chargerType;
        this.cStation = cStation;
    }
    /** 
     * Overload of the {@link #ChargingPoint(Integer, Integer, ChargingStation)} 
     * method with default {@code condition=0}(OPERATIONAL)
     * and {@code isOccupied=false}
     */
    public ChargingPoint(Integer maxOutput, Integer chargerType, ChargingStation cStation) {
         this.maxEnergyOutput = maxOutput; this.chargerType = chargerType; this.cStation = cStation;
        this.currentCondition = 0; this.isOccupied = false;
    }


    public Integer getId() {return this.id;}
    public Condition getCondition() {return Condition.meaningOfCode(this.currentCondition);}
    public Integer getMaxOutput() {return this.maxEnergyOutput;}
    public Boolean isOccupied() {return this.isOccupied;}
    public Integer getChargerType() {return this.chargerType;}
    public ChargingStation getCStation() {return this.cStation;}

    public void setCondition(Condition status) {this.currentCondition = status.value;}
    public void setMaxOutput(Integer newMaxOutput) {this.maxEnergyOutput = newMaxOutput;}
    public void setIsOccupied() {this.isOccupied = true;}
    public void resetIsOccupied() {this.isOccupied = false;}
    public void setChargerType(Integer chargerType) {this.chargerType = chargerType;}
    public void setCStation(ChargingStation cStation) {this.cStation = cStation;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChargingPoint)) return false;
        ChargingPoint c = (ChargingPoint) o;
        return Objects.equals(this.id, c.id);
    }
    @Override
    public int hashCode() {return Objects.hash(this.id);}
    @Override
    public String toString() {return "Point{" + "id=" + this.id + ", chargingStationID=" + this.cStation.getId() + ", Type=" 
                                + this.chargerType + "}";}
}
