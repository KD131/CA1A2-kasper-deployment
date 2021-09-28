/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.Person;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;

/**
 * @author tha
 */
public class PersonPopulator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade personFacade = PersonFacade.getPersonFacade(emf);
        /*personFacade.create(new PersonDTO(new Person(50104201, "norm@macdonnald.com", "Norm", "Macdonnald")));
        personFacade.create(new PersonDTO(new Person(20204151, "conan@obrien.com", "Conan", "O'Brien")));
        personFacade.create(new PersonDTO(new Person(11040615, "Stephen@colbert.com", "Stephen", "Colbert")));*/
    }

    public static void main(String[] args) {
        populate();
    }
}
