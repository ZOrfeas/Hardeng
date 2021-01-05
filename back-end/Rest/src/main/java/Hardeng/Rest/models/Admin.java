package Hardeng.Rest.models;

import java.util.*;
import javax.persistence.*;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column(unique = true, length = 10, nullable = false)
    private String username;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(unique = true, length = 20, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String companyName;
    
    @Column(length = 10, nullable = false)
    private Integer companyPhone;

    @Column(length = 20, nullable = false)
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
     Admin(String username, String password, String email, String companyName, Integer companyPhone, String companyLocation) {
         this.username = username;
         this.password = password;
         this.email = email;
         this.companyName = companyName;
         this.companyPhone = companyPhone;
         this.companyLocation = companyLocation;
     }
     /** 
     * Overload of the {@link #Admin(String, String, String, String, Integer, String)} method with undefined
     * {@code email='undefined-username'}, {@code companyName='undefined'}, 
     * {@code companyPhone=0} and {@code companyLocation='undefined'}
     */
     Admin(String username, String password) {
         this.username = username;
         this.password = password;
         this.email = "undefined-"+ this.username;
         this.companyName = "undefined";
         this.companyPhone = 0;
         this.companyLocation = "undefined";
     }

    /* Getter methods */
    public Integer getAdminId () {
        return this.adminId;
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

    public Integer getCompanyPhone () {
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

    public void setCompanyPhone (Integer companyPhone) {
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
        return Objects.equals(this.adminId, a.adminId) && Objects.equals(this.username, a.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.adminId, this.username);
    }

    @Override
    public String toString() {
        return "Admin{" + "adminId:" + this.adminId + ", username:\"" + this.username + "\"}";
    }
}
