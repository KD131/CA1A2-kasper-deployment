package entities;

import dtos.PhoneDTO;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.io.Serializable;

// TODO see if it fails because of the table name in the query
@Table(name = "PHONE")

@Entity
@NamedQuery(name = "Phone.deleteAllRows", query = "DELETE FROM Phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private String info;

    public Phone() {
    }

    public Phone(int number) {
        this.number = number;
        this.info = "personal";
    }

    public Phone(int number, String info) {
        this.number = number;
        this.info = info;
    }

    public Phone(PhoneDTO phoneDTO) {
        this.id = id;
        this.number = number;
        this.info = info;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean equals(PhoneDTO dto) {
        if (getNumber() != dto.getNumber()) return false;
        if (!getId().equals(dto.getId())) return false;
        return getInfo().equals(dto.getInfo());
    }
}