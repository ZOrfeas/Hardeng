package Hardeng.Rest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
// import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Float longitude;

    @Column(nullable = false)
    private Float latitude;

    @ManyToOne
    private Integer operatorId;

    /** 
     * Stores the current condition of the charging point.
     * <br></br>
     * e.g.: 0->OK, -1->DOWN, 1->UNDER_REPAIRS, 2->DUE_CHECK, 3->UNDER_CONSTRUCTION
     */
    @Column(nullable = false, length = 8)
    private Integer condition; 

    @Column(nullable = false)
    private Integer maxOutput;

    @Column(nullable = false)
    private Boolean isOccupied;

}
