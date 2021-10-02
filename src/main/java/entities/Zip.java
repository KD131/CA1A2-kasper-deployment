package entities;

import dtos.ZipDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ZIP")

@Entity
@NamedQuery(name = "Zip.deleteAllRows", query = "DELETE FROM Zip")
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

    public Zip(ZipDTO zipDTO) {
        this.zip = zipDTO.getZip();
        this.city = zipDTO.getCity();
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

    public boolean equals(ZipDTO dto) {
        if (getZip() != dto.getZip()) return false;
        return getCity().equals(dto.getCity());
    }

}