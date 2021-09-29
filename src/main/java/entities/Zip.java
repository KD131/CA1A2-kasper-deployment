package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ZIP")

@Entity
@NamedQuery(name = "Zip.deleteAllRows", query = "DELETE from Zip")
public class Zip implements Serializable {
    @Id
    private int zip;
    private String city;
    
    @OneToMany(mappedBy = "zip")
    private List<Address> addresses;

    public Zip() {
    }

    public Zip(int zip, String city) {
        this.zip = zip;
        this.city = city;
        this.addresses = new ArrayList<>();
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public List<Address> getAddresses()
    {
        return addresses;
    }
}