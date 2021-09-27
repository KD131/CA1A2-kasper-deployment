package facades;

import dtos.PersonDTO;
import entities.Person;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }


    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PersonDTO create(PersonDTO person) {
        Person personEntity = new Person(person.getName());
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(personEntity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(personEntity);
    }

    public PersonDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        return new PersonDTO(em.find(Person.class, id));
    }

    //TODO Remove/Change this before use
    public long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }

    public List<PersonDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return PersonDTO.getDtos(persons);
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = getPersonFacade(emf);
        fe.getAll().forEach(dto -> System.out.println(dto));
    }
}
