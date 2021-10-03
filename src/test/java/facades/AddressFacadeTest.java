package facades;

import dtos.AddressDTO;
import edu.emory.mathcs.backport.java.util.Arrays;
import entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressFacadeTest {
    private static EntityManagerFactory emf;
    private static AddressFacade facade;

    private static Address a1, a2, a3;
    private static Person p1, p2;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AddressFacade.getAddressFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();

        a1 = new Address("Test street 21",
                new Zip(6969, "Nice-ville"));
        a2 = new Address("2nd and Hill 34",
                new Zip(4242, "Cool-town"));
        a3 = new Address("Test street",
                new Zip(2323, "Test city"));
        // a3 has no persons by design

        Hobby h1 = new Hobby("Skiing", "skiing.com", "General", "Outdoors");
        Hobby h2 = new Hobby("Polo", "polo.com", "Sport", "Outdoors");
        Hobby h3 = new Hobby("Jogging", "jogging.com", "General", "Outdoors");

        p1 = new Person(
                new ArrayList<Phone>(Arrays.asList(new Phone[]{new Phone(11111111)})),
                "bob@bob.com",
                "Bob",
                "Roberts", a1);

        p1.addHobby(h1);
        p1.addHobby(h2);

        List<Phone> phones2 = new ArrayList<>();
        phones2.add(new Phone(22222222));
        p2 = new Person(phones2,
                "alice@alice.com",
                "Alice",
                "Allison", a2);

        p2.addHobby(h2);
        p2.addHobby(h3);

        try {
            em.getTransaction().begin();
            // hopefully there's a better way than use a native query to wipe out the join table
            em.createNativeQuery("DELETE FROM PERSON_PHONE").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.resetPK").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Zip.deleteAllRows").executeUpdate();
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void create() {
    }

    @Test
    void edit() {
        a1.setAddress("Fest street 21");
        AddressDTO a1DTO = new AddressDTO(a1);

        facade.update(a1DTO);
        assertEquals("Fest street 21", facade.getById(a1.getId()).getAddress());
    }
    
    @Test
    void delete() throws Exception
    {
        facade.delete(a3.getId());
        List<AddressDTO> addresses = facade.getAll();
        assertEquals(2, addresses.size());
    }
    
    @Test
    void delete_addressHasPerson()
    {
        assertThrows(Exception.class, () ->
                facade.delete(a2.getId()));
    }

    @Test
    void getById() {
    }

    @Test
    void getByZip() {
    }

    @Test
    void getAddressCount() {
    }

    @Test
    void getAll() {
    }
}