package facades;

import dtos.AddressDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import dtos.ZipDTO;
import edu.emory.mathcs.backport.java.util.Arrays;
import entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonFacadeTest
{
    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    
    private static Person p1;
    private static Person p2;
    
    @BeforeAll
    static void beforeAll()
    {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }
    
    @BeforeEach
    void setUp()
    {
        EntityManager em = emf.createEntityManager();
        
        p1 = new Person(
                new ArrayList<Phone>(Arrays.asList(new Phone[]{new Phone(11111111)})),
                "bob@bob.com",
                "Bob",
                "Roberts",
                new Address("Test street 21",
                        new Zip(6969, "Nice-ville")));
    
        List<Phone> phones2 = new ArrayList<>();
        phones2.add(new Phone(22222222));
        p2 = new Person(phones2,
                "alice@alice.com",
                "Alice",
                "Allison",
                new Address("2nd and Hill 34",
                        new Zip(4242, "Cool-town")));
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
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }
//
//    @AfterEach
//    void tearDown()
//    {
//        EntityManager em = emf.createEntityManager();
//        try
//        {
//            em.remove(p1);
//            em.remove(p2);
//        }
//        finally
//        {
//            em.close();
//        }
//    }
    
    @Test
    void create()
    {
    }
    
    @Test
    void getById()
    {
        PersonDTO person = facade.getById(1);
        assertNotNull(person);
    }
    
    @Test
    void getPersonCount()
    {
        assertEquals(2, facade.getPersonCount());
    }
    
    @Test
    void getAll()
    {
        List<PersonDTO> persons = facade.getAll();
        assertNotNull(persons);
        assertEquals(2, persons.size());
    }
    
    @Test
    void edit()
    {
    }
    
    @Test
    void delete()
    {
    }
    
    @Test
    void getByPhone()
    {
        PhoneDTO phone = new PhoneDTO(11111111, "personal");
        PersonDTO person = facade.getByPhone(phone);
        assertNotNull(person);
        assertEquals("Bob", person.getFirstName());
    }
    
    @Test
    void getByHobby()
    {
    }
    
    @Test
    void getByAddress()
    {
        AddressDTO address = new AddressDTO("Test street 21",
                new Zip(6969, "Nice-ville"));
        List<PersonDTO> persons = facade.getByAddress(address);
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("Bob", persons.get(0).getFirstName());
    }
    
    @Test
    void getByZip()
    {
        ZipDTO zip = new ZipDTO(4242, "Cool-town");
        List<PersonDTO> persons = facade.getByZip(zip);
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("Alice", persons.get(0).getFirstName());
    }
}