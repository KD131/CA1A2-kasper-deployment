package facades;

import dtos.*;
import entities.Person;
import facades.inter.PersonFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements PersonFacadeInterface {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

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

    @Override
    public PersonDTO create(PersonDTO person) {
        Person personEntity = new Person(
                person.getPhones(),
                person.getEmail(),
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getHobbies());
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

    @Override
    public PersonDTO edit(PersonDTO person) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public PersonDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        return new PersonDTO(em.find(Person.class, id));
    }

    @Override
    public PersonDTO getByPhone(PhoneDTO phone) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.phones ph WHERE ph.number = :phone", Person.class); // IN / MEMBER OF ?
        query.setParameter("phone", phone.getNumber());
        Person person = query.getSingleResult();
        return new PersonDTO(person);
    }

    @Override
    public List<PersonDTO> getByHobby(HobbyDTO hobby) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE :hobby MEMBER OF h", Person.class); // IN / MEMBER OF ?
        query.setParameter("hobby", hobby);
        List<Person> persons = query.getResultList();
        return PersonDTO.getDtos(persons);
    }

    @Override
    public List<PersonDTO> getByAddress(AddressDTO address) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a where a = :address", Person.class);
        query.setParameter("address", address);
        List<Person> persons = query.getResultList();
        return PersonDTO.getDtos(persons);
    }

    @Override
    public List<PersonDTO> getByZip(ZipDTO zip) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address.zip z where z.zip = :zip", Person.class);
        query.setParameter("zip", zip.getZip());
        List<Person> persons = query.getResultList();
        return PersonDTO.getDtos(persons);
    }


    @Override
    public List<PersonDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = query.getResultList();
        return PersonDTO.getDtos(persons);
    }

    @Override
    public long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade pf = getPersonFacade(emf);
        pf.getAll().forEach(dto -> System.out.println(dto));
    }
}
