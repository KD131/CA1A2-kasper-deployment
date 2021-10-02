/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Alex & Johan
 * MANUALLY DROP TABLE ADDRESS, PERSON, PERSON_HOBBY & PHONE BEFORE RUNNING (KEEP HOBBY & ZIP)
 */
public class PopulatorPerson {
    private static List<Hobby> getHobbies(EntityManager em) {
        TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
        return (List<Hobby>) query.getResultList();
    }

    private static List<Zip> getZips(EntityManager em) {
        TypedQuery<Zip> query = em.createQuery("SELECT z FROM Zip z", Zip.class);
        return (List<Zip>) query.getResultList();
    }

    private static int getRandomNumber() {
        Random rand = new Random();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            num.append(rand.nextInt(10));
        }
        return Integer.parseInt(num.toString());
    }

    private static Person buildPerson(String email, String fname, String lname, String address, List<Zip> zips, List<Hobby> hobbies) {
        Random rand = new Random();
        List<Phone> pho = new ArrayList<>();
        List<Hobby> hob = new ArrayList();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < 8; i++) num.append(rand.nextInt(10));
        pho.add(new Phone(getRandomNumber()));
        pho.add(new Phone(getRandomNumber(), "work"));
        hob.add(hobbies.get(rand.nextInt(hobbies.size())));
        hob.add(hobbies.get(rand.nextInt(hobbies.size())));
        return new Person(pho, email, fname, lname, new Address(address, zips.get(rand.nextInt(zips.size()))), hob);
    }

    public static String populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        try {
            List<Hobby> hobbies = getHobbies(em);
            List<Zip> zips = getZips(em);
            List<Person> persons = new ArrayList();

            persons.add(buildPerson(
                    "bente@bentsenogco.dk",
                    "Bente",
                    "Bentsen",
                    "Lysallen 246",
                    zips, hobbies));

            persons.add(buildPerson(
                    "gammelSteen@hotmail.com",
                    "Steen",
                    "Aldermann",
                    "Skuldelevvej 6, st. t.h.",
                    zips, hobbies));

            persons.add(buildPerson(
                    "kasper@christensen.dk",
                    "Kasper",
                    "Christensen",
                    "Rolighedsvej 12, 3. t.v.",
                    zips, hobbies));

            persons.add(buildPerson(
                    "mmmonaco@gmail.com",
                    "Monica",
                    "Kirkegaard",
                    "Kirsebærsvej 101, 1. t.h.",
                    zips, hobbies));

            persons.add(buildPerson(
                    "malibro@hotmail.dk",
                    "Malou",
                    "Brohammer",
                    "Vestergade 19, 5. t.v.",
                    zips, hobbies));

            persons.add(buildPerson(
                    "mikki1999@gmail.com",
                    "Mikkel",
                    "Trove",
                    "Skomagervej 164",
                    zips, hobbies));

            persons.add(buildPerson(
                    "eriksvend72@gmail.com",
                    "Erik",
                    "Svendsen",
                    "Teglvej 5",
                    zips, hobbies));

            em.getTransaction().begin();
            // hopefully there's a better way than use a native query to wipe out the join table
            em.createNativeQuery("DELETE FROM PERSON_PHONE").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.resetPK").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            for (Person person : persons) {
                em.persist(person);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            em.close();
        }
        return "Succes";
    }

    public static void main(String[] args) {
        String popped = populate();
    }
}