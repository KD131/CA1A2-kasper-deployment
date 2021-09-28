package entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;

    private Zip zip;

    public Address() {
    }

    public Address(String address, Zip zip) {
        this.address = address;
        this.zip = zip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Zip getZip() {
        return zip;
    }

    public void setZip(Zip zip) {
        this.zip = zip;
    }
}