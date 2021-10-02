package entities;

import dtos.AddressDTO;
import dtos.ZipDTO;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ADDRESS")

@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE FROM Address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Zip zip;
    
    @OneToMany(mappedBy = "address")
    private List<Person> persons;

    public Address() {
    }

    public Address(String address, Zip zip) {
        this.address = address;
        this.zip = zip;
        this.persons = new ArrayList<>();
    }

    public Address address(AddressDTO addressDTO) {
        if(addressDTO.getId() != 0) {
            this.id = addressDTO.getId();
            this.address = addressDTO.getAddress();
            this.zip = updateZipDTOToEntity(addressDTO.getZip());
        }
        return this;
    }

    public Zip updateZipDTOToEntity(ZipDTO zipDTO) {
        Zip zip = new Zip();
        zip.setZip(zipDTO.getZip());
        zip.setCity(zip.getCity());

        return zip;
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
    
    public List<Person> getPersons()
    {
        return persons;
    }

    public boolean equals(AddressDTO dto) {
        if (!getId().equals(dto.getId())) return false;
        if (!getAddress().equals(dto.getAddress())) return false;
        return getZip().equals(dto.getZip());
    }
}