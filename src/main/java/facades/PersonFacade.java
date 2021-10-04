package facades;

import dtos.*;
import entities.*;
import facades.inter.PersonFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
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
    public PersonDTO create(PersonDTO personDTO) throws Exception {
        AddressFacade ADDRESS_FACADE = AddressFacade.getAddressFacade(emf);
        EntityManager em = emf.createEntityManager();

        // check if address already exist. If it doesn't, create new address and get managed entity.
        AddressDTO aDto = ADDRESS_FACADE.getByFields(personDTO.getAddress());
        Address aEntity = null;
        if (aDto == null) {
            aDto = ADDRESS_FACADE.create(personDTO.getAddress());
        }
        aEntity = em.find(Address.class, aDto.getId());

        try {
            Person person = new Person(personDTO);
            person.setAddress(aEntity);
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO update(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = new Person(personDTO);
            if (personDTO.getId() == getById(personDTO.getId()).getId()) {
                em.getTransaction().begin();
                em.merge(person);
                em.getTransaction().commit();
                return new PersonDTO(person);
            } else throw new WebApplicationException("Person not found", 404);
        } catch (Exception e) {
            throw new WebApplicationException("Transaction failed", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO delete(long id) {

        EntityManager em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) throw new WebApplicationException("Person not found", 404);
            PersonDTO personDTO = new PersonDTO(person);
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            em.clear();
            return personDTO;
        } catch (Exception e) {
            throw new WebApplicationException("Transaction failed", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) throw new WebApplicationException("Person not found", 404);
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getByPhone(PhoneDTO phoneDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Phone phone = new Phone(phoneDTO);
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE :phone MEMBER OF p.phones", Person.class);
            query.setParameter("phone", phone);
            Person person = query.getSingleResult();
            return new PersonDTO(person);
        } catch (Exception e) {
            if (e instanceof NoResultException) throw new WebApplicationException("Person not found", 404);
            else throw new WebApplicationException("Request failed", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByHobby(HobbyDTO hobbyDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Hobby hobby = new Hobby(hobbyDTO);
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE :hobby MEMBER OF p.hobbies", Person.class);
            query.setParameter("hobby", hobby);
            List<Person> persons = query.getResultList();
            if (persons.size() == 0) throw new WebApplicationException("Persons not found", 404);
            return PersonDTO.getDtos(persons);
        } catch (Exception e) {
            throw new WebApplicationException("Request failed", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByAddress(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Address address = new Address(addressDTO);
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address = :address", Person.class);
            query.setParameter("address", address);
            List<Person> persons = query.getResultList();
            if (persons.size() == 0) throw new WebApplicationException("Persons not found", 404);
            return PersonDTO.getDtos(persons);
        } catch (Exception e) {
            throw new WebApplicationException("Request failed", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getByZip(ZipDTO zipDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Zip zip = new Zip(zipDTO);
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address.zip = :zip", Person.class);
            query.setParameter("zip", zip);
            List<Person> persons = query.getResultList();
            if (persons.size() == 0) throw new WebApplicationException("Persons not found", 404);
            return PersonDTO.getDtos(persons);
        } catch (Exception e) {
            throw new WebApplicationException("Request failed", 500);
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
            if (persons.size() == 0) throw new WebApplicationException("Persons not found", 404);
            return PersonDTO.getDtos(persons);
        } catch (Exception e) {
            throw new WebApplicationException("Request failed", 500);
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
        } catch (Exception e) {
            throw new WebApplicationException("Request failed", 500);
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
