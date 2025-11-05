package Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Shipping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userid;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String address1;
    private String address2;
    private String landmark;

    @ManyToOne
    @JoinColumn(name = "cityid")
    private City city;

    private String pincode;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserid() { return userid; }
    public void setUserid(int userid) { this.userid = userid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
