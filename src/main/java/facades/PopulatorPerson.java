/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import edu.emory.mathcs.backport.java.util.Arrays;
import entities.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class PopulatorPerson {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        List<Hobby> hobby1 = new ArrayList<>();
        hobby1.add(new Hobby("Håndbold","https://en.wikipedia.org/wiki/Handball","Generel"," Konkurrence"));
        hobby1.add(new Hobby("Spøgelsesjagt","https://en.wikipedia.org/wiki/Ghost_hunting","Generel","Udendørs"));
        List<Phone> phones1 = new ArrayList<>();
        phones1.add(new Phone(11111111));
        phones1.add(new Phone(11111112));
        Person p1 = new Person(phones1,
                "Bente@ko.dk",
                "Bente",
                "Ko",
                new Address("Lysallen 246",
                        new Zip(4000, "Roskilde")), hobby1);

        List<Hobby> hobby2 = new ArrayList<>();
        hobby2.add(new Hobby("Herping","https://en.wikipedia.org/wiki/Herping","Generel"," Observation"));
        hobby2.add(new Hobby("Højeffektiv raket","https://en.wikipedia.org/wiki/High-power_rocketry","Generel","Udendørs"));
        List<Phone> phones2 = new ArrayList<>();
        phones2.add(new Phone(22222221));
        phones2.add(new Phone(22222222));
        Person p2 = new Person(phones2,
                "JensVejmand@hotmail.com",
                "Jens",
                "Vejmand",
                new Address("Skuldelevvej 6",
                        new Zip(4050, "Skibby")), hobby2);

        List<Hobby> hobby3 = new ArrayList<>();
        hobby3.add(new Hobby("Fodbold","https://en.wikipedia.org/wiki/Soccer","Generel","Udendørs"));
        hobby3.add(new Hobby("Freestyle fodbold","https://en.wikipedia.org/wiki/Freestyle_football","Generel","Udendørs"));
        List<Phone> phones3 = new ArrayList<>();
        phones2.add(new Phone(33333331));
        phones2.add(new Phone(33333332));
        Person p3 = new Person(phones2,
                "JensVejmand@hotmail.com",
                "Jens",
                "Vejmand",
                new Address("Skuldelevvej 6",
                        new Zip(4050, "Skibby")), hobby2);

        try
        {
            em.getTransaction().begin();
            // needs either cascading delete or more delete queries to take out the other tables
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.resetPK").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Zip.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    public static void main(String[] args) {
        populate();
    }
}