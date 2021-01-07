package Hardeng.Rest.models;

import java.util.*;
import javax.persistence.*;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 32, nullable = false)
    private String username;
    
    @Column(length = 64, nullable = false)
    private String password;

    @Column(unique = true, length = 32, nullable = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String companyName;
    
    @Column(length = 20, nullable = true)
    private String companyPhone;

    @Column(length = 50, nullable = true)
    private String companyLocation;

    Admin() {}
    /**
     * Admin entity constructor.
     * @param username self-explanatory
     * @param password self-explanatory
     * @param email self-explanatory
     * @param companyName self-explanatory
     * @param companyPhone self-explanatory
     * @param companyLocation The address of the company
    */
    public Admin(String username, String password, String email, String companyName, String companyPhone, String companyLocation) {
         this.username = username;
         this.password = password;
         this.email = email;
         this.companyName = companyName;
         this.companyPhone = companyPhone;
         this.companyLocation = companyLocation;
     }
     /** 
     * Overload of the {@link #Admin(String, String, String, String, Integer, String)} method with undefined
     * {@code email=null}, {@code companyName='undefined'}, 
     * {@code companyPhone=null} and {@code companyLocation=null}
     */
    public Admin(String username, String password) {
         this.username = username;
         this.password = password;
         this.email = null;
         this.companyName = "undefined";
         this.companyPhone = null;
         this.companyLocation = null;
     }

    /* Getter methods */
    public Integer getId () {
        return this.id;
    }

    public String getUsername () {
        return this.username;
    }

    public String getPassword () {
        return this.password;
    }

    public String getEmail () {
        return this.email;
    }

    public String getCompanyName () {
        return this.companyName;
    }

    public String getCompanyPhone () {
        return this.companyPhone;
    }

    public String getCompanyLocation () {
        return this.companyLocation;
    }

    /* Setter methods */
    public void setUsername (String username) {
        this.username = username;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setCompanyName (String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyPhone (String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public void setCompanyLocation (String companyLocation) {
        this.companyLocation = companyLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin a = (Admin) o;
        return Objects.equals(this.id, a.id) && Objects.equals(this.username, a.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username);
    }

    @Override
    public String toString() {
        return "Admin{" + "adminId:" + this.id + ", username:\"" + this.username + "\"}";
    }
}
