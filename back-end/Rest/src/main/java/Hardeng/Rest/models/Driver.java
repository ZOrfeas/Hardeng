package Hardeng.Rest.models;

import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
    private Integer password;
    
    @Column(unique = true, length = 32, nullable = false)
    private String email;

    @Column
    private Integer bonusPoints;

    @Column(unique = false, length = 32, nullable = true)
    private Integer cardID;

    @Column(unique = false, length = 32, nullable = true)
    private Integer walletID;


    //we use FetchType.EAGER (instead of lazy) to load all pricePolicies together
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(     //Creates a table that stores both primary keys
            name = "PricePolicy_Driver",
            joinColumns = {@JoinColumn(name = "driverID")},
            inverseJoinColumns = {@JoinColumn(name = "pricePolicyID")}
    )
    private Set<PricePolicy> pricePolicies; //= new HashSet<>();;

    
    Driver(){}

    public Driver(String driverName, String username, String password, String email, Integer bonusPoints, Integer cardID, Integer walletID){
        this.driverName = driverName; this.username = username; this.password = Objects.hash(password); this.email = email; 
        this.bonusPoints = bonusPoints; this.cardID = cardID; this.walletID = walletID;
    }

    public Driver(String driverName, String username, String password, String email, Integer cardID, Integer walletID){
        this.driverName = driverName; this.username = username; this.password = Objects.hash(password); this.email = email; 
        this.bonusPoints = 0; this.cardID = cardID; this.walletID = walletID;
    } //maybe walletID = null
    

    public Integer getID() {return this.id;}
    public String getDriverName(){return this.driverName;}
    public String getUsername(){return this.username;}
    public String getEmail(){return this.email;}
    public Integer getPassword(){return this.password;}
    public Integer getCardID(){return this.cardID;}
    public Integer getWalletID(){return this.walletID;}
    public Integer getBonusPoints(){return this.bonusPoints;}
    public Set<PricePolicy> getPricePolicy(){return pricePolicies;}


    public void setDriverName(String newDriverName){this.driverName = newDriverName;}
    public void setUsername(String newUsername){this.username = newUsername;}
    public void setEmail(String newEmail){this.email = newEmail;}
    public void setPassword(String newPassword){this.password = Objects.hash(newPassword);}
    public void setCardID(Integer newCardID){this.cardID = newCardID;}
    public void setWalletID(Integer newWalletID){this.walletID = newWalletID;}
    public void setBonusPoints(Integer newBonusPoints){this.bonusPoints = newBonusPoints;}

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