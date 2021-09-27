/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class PersonDTO {
    private long id;
    private String name;

    public PersonDTO(String name) {
        this.name = name;
    }

    public static List<PersonDTO> getDtos(List<Person> persons) {
        List<PersonDTO> personDTOs = new ArrayList();
        persons.forEach(person -> personDTOs.add(new PersonDTO(person)));
        return personDTOs;
    }


    public PersonDTO(Person person) {
        if (person.getId() != null)
            this.id = person.getId();
        this.name = person.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
