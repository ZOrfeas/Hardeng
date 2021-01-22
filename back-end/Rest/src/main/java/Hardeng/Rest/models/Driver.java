package Hardeng.Rest.models;

import java.util.HashSet;
import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.util.Set;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String driverName;

    @Column(unique = true, length = 32, nullable = false)
    private String username;
    
    @Column(length = 64, nullable = false)
    private String password;
    
    @Column(unique = true, length = 32, nullable = false)
    private String email;

    @Column(nullable = true)
    private Integer bonusPoints;

    @Column(unique = false, length = 32, nullable = true)
    private Long cardID;

    @Column(unique = false, length = 32, nullable = true)
    private Long walletID;


    //we use FetchType.EAGER (instead of lazy) to load all pricePolicies together
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(     //Creates a table that stores both primary keys
            name = "PricePolicy_Driver",
            joinColumns = {@JoinColumn(name = "driverID")},
            inverseJoinColumns = {@JoinColumn(name = "pricePolicyID")}
    )
    private Set<PricePolicy> pricePolicies = new HashSet<>();

    //Cars belonging to Driver
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "driver",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<CarDriver> driverCars = new HashSet<>();

    Driver(){}

    public Driver(String driverName, String username, String password, String email, Integer bonusPoints, Long cardID, Long walletID){
        this.driverName = driverName; this.username = username; this.password = password; this.email = email; 
        this.bonusPoints = bonusPoints; this.cardID = cardID; this.walletID = walletID;
    }

    public Driver(String driverName, String username, String password, String email){
        this(driverName,username,password,email,0,null,null);
    }
    

    public Integer getID() {return this.id;}
    public String getDriverName(){return this.driverName;}
    public String getUsername(){return this.username;}
    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
    public Long getCardID(){return this.cardID;}
    public Long getWalletID(){return this.walletID;}
    public Integer getBonusPoints(){return this.bonusPoints;}
    public Set<PricePolicy> getPricePolicies(){return pricePolicies;}
    public Set<CarDriver> getCars() {return driverCars;} 


    public void setDriverName(String newDriverName){this.driverName = newDriverName;}
    public void setUsername(String newUsername){this.username = newUsername;}
    public void setEmail(String newEmail){this.email = newEmail;}
    public void setPassword(String newPassword){this.password = newPassword;}
    public void setCardID(Long newCardID){this.cardID = newCardID;}
    public void setWalletID(Long newWalletID){this.walletID = newWalletID;}
    public void setBonusPoints(Integer newBonusPoints){this.bonusPoints = newBonusPoints;}

    /**
     * Handles the addition of a PricePolicy to a Driver
     * @param newPricePolicy PricePolicy to be added
     */
    public void addPricePolicy(PricePolicy newPricePolicy) {
        this.pricePolicies.add(newPricePolicy);
        newPricePolicy.getDrivers().add(this);
    }

    /**
     * Handles the removal of a PricePolicy from a Driver
     * @param rmPricePolicy PricePolicy to be removed
     */
    public void removePricePolicy(PricePolicy rmPricePolicy) {
        this.pricePolicies.remove(rmPricePolicy);
        rmPricePolicy.getDrivers().remove(this);
    }
    
    /**
     * Handles the addition of a Car to a Driver
     * @param car Car to be added
     * @param currentCap CurrentCapacity of vehicle
     */
    public void addCar(Car car, Double currentCap) {
        CarDriver carDriver = new CarDriver(this, car, currentCap);
        driverCars.add(carDriver);
        car.getDrivers().add(carDriver);
    }

    /**
     * Handles the removal of a Car from a Driver
     * <br></br>
     * NOTE! may need iteration over set to find the
     * propper CarDriver Entity to remove !ETON
     * @param rmCar Car to be removed
     */
     public void removeCar(Car rmCar) {
        this.driverCars.remove(rmCar);
        rmCar.getDrivers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        Driver u = (Driver) o;
        return Objects.equals(this.id, u.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Driver{" + "id=" + this.id + ", name='" + this.driverName + "'}";
    }
}