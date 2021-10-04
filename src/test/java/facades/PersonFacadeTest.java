package facades;

import dtos.*;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
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
                Arrays.asList(new Phone(11111111)),
                "bob@bob.com",
                "Bob",
                "Roberts",
                new Address("Test street 21",
                        new Zip(6969, "Nice-ville")));
        
        p1.addHobby(h1);
        p1.addHobby(h2);
        
        p2 = new Person(
                Arrays.asList(new Phone(22222222)),
                "alice@alice.com",
                "Alice",
                "Allison",
                new Address("2nd and Hill 34",
                        new Zip(4242, "Cool-town")));
        
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
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void create_newAddress_newZip() {
        PersonDTO person = new PersonDTO(
                Arrays.asList(new PhoneDTO(34343434, "work")),
                "lars@larsen.lars",
                "Lars",
                "Larsen",
                new AddressDTO("Lars street",
                        new ZipDTO(1234, "Lars city")));

        Exception ex = assertThrows(Exception.class,
                () -> facade.create(person));
        assertEquals("ZIP code does not exist in the database: " + person.getAddress().getZip().getId(), ex.getMessage());
    }

    @Test
    void create_newAddress_existingZip() throws Exception {
        PersonDTO person = new PersonDTO(
                Arrays.asList(new PhoneDTO(33333333, "work")),
                "chad@chad.com",
                "Chad",
                "Kroeger",
                new AddressDTO("Someday 42",
                        new ZipDTO(p2.getAddress().getZip())));

        PersonDTO created = facade.create(person);
        assertNotNull(created);
        assertEquals(person.getFirstName(), created.getFirstName());

        PersonDTO fromDb = facade.getById(created.getId());

        assertNotNull(fromDb);
        assertEquals(created.getFirstName(), fromDb.getFirstName());
    }

    @Test
    void create_existingAddress() throws Exception {
        List<PhoneDTO> phones = new ArrayList<>();
        phones.add(new PhoneDTO(34343434, "work"));
        PersonDTO person = new PersonDTO(
                phones,
                "lars@larsen.lars",
                "Lars",
                "Larsen",
                new AddressDTO(p1.getAddress()));
        PersonDTO created = facade.create(person);
        assertNotNull(created);
        assertEquals(person.getFirstName(), created.getFirstName());

        PersonDTO fromDb = facade.getById(created.getId());

        assertNotNull(fromDb);
        assertEquals(created.getFirstName(), fromDb.getFirstName());
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

        facade.update(p2DTO);
        assertEquals("Allis", facade.getById(p2.getId()).getLastName());
    }
    
    // if a person is deleted, their address cascade deletes
    // oddly, the Zip's city name is null regardless of whether we delete anything
    @Test
    void editAddressOnOneAndRemovePerson() {
        Address address = new Address("Test street", new Zip(2323, "Test city"));
        p1.setAddress(address);
        PersonDTO p1DTO = new PersonDTO(p1);

        facade.update(p1DTO);
        assertEquals(p1.getAddress().getAddress(), facade.getById(p1.getId()).getAddress().getAddress());
        
        facade.delete(p1.getId());
        
        AddressFacade af = AddressFacade.getAddressFacade(emf);
        assertEquals(2, af.getAll().size());
    }
    
    // if two persons have same address, and one is deleted, the address is not deleted
    @Test
    void editAddressOnTwoAndRemovePerson() {
        Address address = new Address("Test street", new Zip(2323, "Test city"));
        p1.setAddress(address);
        p2.setAddress(address);
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);

        facade.update(p1DTO);
        assertEquals(p1.getAddress().getAddress(), facade.getById(p1.getId()).getAddress().getAddress());
        facade.update(p2DTO);
        assertEquals(p2.getAddress().getAddress(), facade.getById(p2.getId()).getAddress().getAddress());
        
        facade.delete(p1.getId());
    
        AddressFacade af = AddressFacade.getAddressFacade(emf);
        assertEquals(3, af.getAll().size());
    }

    @Test
    void delete() {
        facade.delete(p2.getId());
        List<PersonDTO> persons = facade.getAll();
        assertEquals(1, persons.size());
        assertEquals(p1.getFirstName(), persons.get(0).getFirstName());
        assertTrue(p1.equals(persons.get(0)));
        assertTrue(persons.get(0).equals(p1));
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
        AddressDTO address = new AddressDTO(p1.getAddress());
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