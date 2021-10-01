package facades;

import dtos.*;
import edu.emory.mathcs.backport.java.util.Arrays;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest {
    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    private static Person p1, p2;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        
        Hobby h1 = new Hobby("Skiing", "skiing.com", "General", "Outdoors");
        Hobby h2 = new Hobby("Polo", "polo.com", "Sport", "Outdoors");
        Hobby h3 = new Hobby("Jogging", "jogging.com", "General", "Outdoors");

        p1 = new Person(
                new ArrayList<Phone>(Arrays.asList(new Phone[]{new Phone(11111111)})),
                "bob@bob.com",
                "Bob",
                "Roberts",
                new Address("Test street 21",
                        new Zip(6969, "Nice-ville")));
        
        p1.addHobby(h1);
        p1.addHobby(h2);
        
        List<Phone> phones2 = new ArrayList<>();
        phones2.add(new Phone(22222222));
        p2 = new Person(phones2,
                "alice@alice.com",
                "Alice",
                "Allison",
                new Address("2nd and Hill 34",
                        new Zip(4242, "Cool-town")));
        
        p2.addHobby(h2);
        p2.addHobby(h3);
        
        try {
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
        } finally {
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
    void create() {
    }

    @Test
    void getById() {
        PersonDTO person = facade.getById(p1.getId());
        assertNotNull(person);
        assertEquals(p1.getFirstName(), person.getFirstName());
    }

    @Test
    void getPersonCount() {
        assertEquals(2, facade.getPersonCount());
    }

    @Test
    void getAll() {
        List<PersonDTO> persons = facade.getAll();
        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    void edit() {
        p2.setLastName("Allis");
        PersonDTO p2DTO = new PersonDTO(p2);

        facade.edit(p2DTO);
        assertEquals("Allis", facade.getById(p2.getId()).getLastName());
    }

    @Test
    void delete(long id) {
      /*  try {
            AddressDTO addressDTO = new AddressDTO(em.find(Address.class, id));
            if(addressDTO != null) {
                em.remove(addressDTO);
            }
        } finally {
            em.close();
        }*/
    }

    @Test
    void getByPhone() {
        PhoneDTO phone = new PhoneDTO(p1.getPhones().get(0));
        PersonDTO person = facade.getByPhone(phone);
        assertNotNull(person);
        assertEquals(p1.getFirstName(), person.getFirstName());
    }

    @Test
    void getByHobby() {
        HobbyDTO hobby1 = new HobbyDTO(p1.getHobbies().get(1)); // h2
        List<PersonDTO> persons1 = facade.getByHobby(hobby1);
        assertNotNull(persons1);
        assertEquals(2, persons1.size());
        // assertThat(persons1, containsInAnyOrder(new PersonDTO(p1), new PersonDTO(p2));
        
        HobbyDTO hobby2 = new HobbyDTO(p2.getHobbies().get(1)); // h3
        List<PersonDTO> persons2 = facade.getByHobby(hobby2);
        assertNotNull(persons2);
        assertEquals(1, persons2.size());
        
    }

    @Test
    void getByAddress() {
        AddressDTO address = new AddressDTO("Test street 21",
                new ZipDTO(6969, "Nice-ville"));
        List<PersonDTO> persons = facade.getByAddress(address);
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("Bob", persons.get(0).getFirstName());
    }

    @Test
    void getByZip() {
        ZipDTO zip = new ZipDTO(4242, "Cool-town");
        List<PersonDTO> persons = facade.getByZip(zip);
        assertNotNull(persons);
        assertEquals(1, persons.size());
        assertEquals("Alice", persons.get(0).getFirstName());
    }
}