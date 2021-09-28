package facades;

import entities.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest
{
    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    
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
        try
        {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows");
            em.createNamedQuery("Person.resetPK");
            em.persist(new Person(11111111, "bob@bob.com", "Bob", "Roberts"));
            em.persist(new Person(22222222, "alice@alice.com", "Alice", "Allison"));
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }
    
    @Test
    void create()
    {
    }
    
    @Test
    void getById()
    {
    }
    
    @Test
    void getPersonCount()
    {
        assertEquals(2, facade.getPersonCount());
    }
    
    @Test
    void getAll()
    {
    }
}