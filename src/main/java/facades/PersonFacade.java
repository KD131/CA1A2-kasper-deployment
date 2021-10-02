package facades;

import dtos.*;
import entities.Hobby;
import entities.Person;
import facades.inter.PersonFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

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
    public PersonDTO create(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        Person person = new Person();
        try {
            person.person(personDTO);
            
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO edit(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = new Person();
            person.person(personDTO);
            if (personDTO.getId() == getById(personDTO.getId()).getId()) {
                em.getTransaction().begin();
                em.merge(person);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
            return personDTO;
        }
    }

    @Override
    public void delete(long id) {

        EntityManager em = emf.createEntityManager();
        try {
            if(getById(id) != null) {
                em.getTransaction().begin();
                Person p = em.find(Person.class, id);
                em.remove(p);
                em.getTransaction().commit();
                em.clear();
            }
        } finally {
            em.close();
        }
    }



    @Override
    public PersonDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new PersonDTO(em.find(Person.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getByPhone(PhoneDTO phone) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.phones ph WHERE ph.number = :phone", Person.class); // IN / MEMBER OF ?
            query.setParameter("phone", phone.getNumber());
            Person person = query.getSingleResult();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByHobby(HobbyDTO hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            Hobby hobbyEntity = em.find(Hobby.class, hobby.getId());
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE :hobby MEMBER OF p.hobbies", Person.class); // IN / MEMBER OF ?
            query.setParameter("hobby", hobbyEntity);
            List<Person> persons = query.getResultList();
            return PersonDTO.getDtos(persons);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByAddress(AddressDTO address) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a where a.zip.zip = :zip AND a.address = :address", Person.class);
            query.setParameter("zip", address.getZip().getId());
            query.setParameter("address", address.getAddress());
            List<Person> persons = query.getResultList();
            return PersonDTO.getDtos(persons);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByZip(ZipDTO zip) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address.zip z where z.id = :zip", Person.class);
            query.setParameter("zip", zip.getId());
            List<Person> persons = query.getResultList();
            return PersonDTO.getDtos(persons);
        } finally {
            em.close();
        }
    }


    @Override
    public List<PersonDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> persons = query.getResultList();
            return PersonDTO.getDtos(persons);
        } finally {
            em.close();
        }
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
