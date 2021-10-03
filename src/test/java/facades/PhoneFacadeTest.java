package facades;

import dtos.AddressDTO;
import dtos.PhoneDTO;
import entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneFacadeTest {
    private static EntityManagerFactory emf;
    private static PhoneFacade facade;

    private static Phone p1, p2;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhoneFacade.getPhoneFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Phone(11111111, "Business");
        p2 = new Phone(22222222, "Pleasure");

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
    void create() {
    }

    @Test
    void edit() {
        p1.setInfo("Bees knees");
        PhoneDTO p1DTO = new PhoneDTO(p1);

        facade.edit(p1DTO);
        assertEquals("Bees knees", facade.getById(p1.getId()).getInfo());
    }

    @Test
    void delete() {
        facade.delete(p2.getId());
        List<PhoneDTO> phones = facade.getAll();
        assertEquals(1, phones.size());
        assertEquals(p1.getInfo(), phones.get(0).getInfo());
    }

    @Test
    void getById() {
    }

    @Test
    void getByNumber() {
    }

    @Test
    void getByPerson() {
    }

    @Test
    void getByPersonId() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getPhoneCount() {
    }
}