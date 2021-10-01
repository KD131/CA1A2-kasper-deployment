package entities;

import dtos.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "PERSON")

@Entity
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE FROM Person")
@NamedNativeQuery(name = "Person.resetPK", query = "ALTER TABLE PERSON AUTO_INCREMENT = 1")

@SqlResultSetMapping(name="updateResult", columns = { @ColumnResult(name = "count")})

@NamedNativeQueries({
        @NamedNativeQuery(
                name    =   "updatePerson",
                query   =   "UPDATE Person SET email = ?, firstName = ?, lastName = ?, hobbies = ?, phones = ?, address = ? WHERE id = ?"
                ,resultSetMapping = "updateResult"
        )
})

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "PERSON_HOBBY")
    private List<Hobby> hobbies;

    @OneToMany(orphanRemoval = true,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "PERSON_PHONE")
    private List<Phone> phones;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE,
            CascadeType.MERGE
    })
    private Address address;

    public Person() {
    }

    public Person(List<Phone> phones, String email, String firstName, String lastName, Address address, List<Hobby> hobbies) {
        this.phones = new ArrayList<>();
        phones.forEach(this::addPhone);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.hobbies = hobbies;
    }

    public Person(List<Phone> phones, String email, String firstName, String lastName, Address address) {
        this.phones = new ArrayList<>();
        phones.forEach(this::addPhone);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.hobbies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        if (phone != null) {
            this.phones.add(phone);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address != null) {
            this.address = address;
            address.getPersons().add(this);
        }
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public void addHobby(Hobby hobby) {
        if (hobby != null) {
            this.hobbies.add(hobby);
            hobby.getPersons().add(this);
        }
    }

    public void removeHobby(Hobby hobby) {
        if (hobby != null) {
            this.hobbies.remove(hobby);
            hobby.getPersons().remove(this);
        }
    }

    public boolean equals(PersonDTO dto) {
        if (!getId().equals(dto.getId())) return false;
        if (!getEmail().equals(dto.getEmail())) return false;
        if (!getFirstName().equals(dto.getFirstName())) return false;
        if (!getLastName().equals(dto.getLastName())) return false;
        if (!getHobbies().equals(dto.getHobbies())) return false;
        if (!getPhones().equals(dto.getPhones())) return false;
        return getAddress().equals(dto.getAddress());
    }

    public Person person(PersonDTO personDTO){
        if(personDTO.getId() != 0) {
            this.id = personDTO.getId();
            this.email = personDTO.getEmail();
            this.firstName = personDTO.getFirstName();
            this.lastName = personDTO.getLastName();
            this.hobbies = updateHobbyDTOToEntity(personDTO.getHobbies());
            this.phones = updatePhonesDTOToEntity(personDTO.getPhones());
            this.address = updateAddressDTOToEntity(personDTO.getAddress());
            }
        return this;
        }

    public List<Hobby> updateHobbyDTOToEntity(List<HobbyDTO> hobbiesDTO) {
        List<Hobby> hobbies = new ArrayList<>();
        for (HobbyDTO h : hobbiesDTO) {
            hobbies.add(new Hobby(h));
        }
        return hobbies;
    }

    public List<Phone> updatePhonesDTOToEntity(List<PhoneDTO> phonesDTO) {
        List<Phone> phones = new ArrayList<>();
        for (PhoneDTO p : phonesDTO) {
            phones.add(new Phone(p));
        }
        return phones;
    }

    public Address updateAddressDTOToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setAddress(addressDTO.getAddress());
        address.setZip(updateZipDTOToEntity(addressDTO.getZip()));

        return address;
    }

    public Zip updateZipDTOToEntity(ZipDTO zipDTO) {
        Zip zip = new Zip();
        zip.setZip(zipDTO.getZip());
        zip.setCity(zip.getCity());

        return zip;
    }

}
