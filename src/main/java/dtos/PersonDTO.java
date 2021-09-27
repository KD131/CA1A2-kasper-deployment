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
 *
 * @author tha
 */
public class PersonDTO {
    private long id;
    private String str1;
    private String str2;

    public PersonDTO(String dummyStr1, String dummyStr2) {
        this.str1 = dummyStr1;
        this.str2 = dummyStr2;
    }

    public static List<PersonDTO> getDtos(List<Person> persons){
        List<PersonDTO> personDTOs = new ArrayList();
        persons.forEach(person->personDTOs.add(new PersonDTO(person)));
        return personDTOs;
    }


    public PersonDTO(Person person) {
        if(person.getId() != null)
            this.id = person.getId();
        this.str1 = person.getDummyStr1();
        this.str2 = person.getDummyStr2();
    }

    public String getDummyStr1() {
        return str1;
    }

    public void setDummyStr1(String dummyStr1) {
        this.str1 = dummyStr1;
    }

    public String getDummyStr2() {
        return str2;
    }

    public void setDummyStr2(String dummyStr2) {
        this.str2 = dummyStr2;
    }

    @Override
    public String toString() {
        return "PersonDTO{" + "id=" + id + ", str1=" + str1 + ", str2=" + str2 + '}';
    }
    
    
    
    
    
    
}
