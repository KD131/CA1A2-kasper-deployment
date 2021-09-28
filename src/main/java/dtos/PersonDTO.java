/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Address;
import entities.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class PersonDTO {
    private long id;
    private int number;
    private String email;
    private String firstName;
    private String lastName;
    private Address address;

    public PersonDTO(int number, String email, String firstName, String lastName, Address address) {
        this.number = number;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public static List<PersonDTO> getDtos(List<Person> persons) {
        List<PersonDTO> personDTOs = new ArrayList();
        persons.forEach(person -> personDTOs.add(new PersonDTO(person)));
        return personDTOs;
    }


    public PersonDTO(Person person) {
        if (person.getId() != null)
            this.id = person.getId();
            this.number = person.getNumber();
            this.email = person.getEmail();
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.address = person.getAddress();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
        this.address = address;
    }
}
