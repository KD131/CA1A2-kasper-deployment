package entities;

import javax.persistence.*;
import java.io.Serializable;

// TODO see if it fails because of the table name in the query
@Table(name = "phone")

@Entity
@NamedQuery(name = "Phone.deleteAllRows", query = "DELETE from Phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private String info;
    
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REMOVE,
            CascadeType.MERGE
    })
    private Person person;

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
    
    public Person getPerson()
    {
        return person;
    }
    
    public void setPerson(Person person)
    {
        this.person = person;
    }
}