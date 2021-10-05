package facades;

import dtos.*;
import entities.*;
import facades.inter.HobbyFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class HobbyFacade implements HobbyFacadeInterface {


    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getHobbyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public HobbyDTO create(HobbyDTO hobbyDTO) {
        Hobby hobbyEntity = new Hobby(hobbyDTO);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobbyEntity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(hobbyEntity);
    }

    @Override
    public HobbyDTO update(HobbyDTO hobbyDTO) {
        EntityManager em = emf.createEntityManager();
        Hobby original = em.find(Hobby.class, hobbyDTO.getId());
        Hobby hobbyEntity = new Hobby(hobbyDTO);
        try {
            if (original != null) {
                /* More complex, bidirectional removal and setting. Unneeded in this situation.
                hobbyEntity.setPersonsBi(original.getPersons());
                original.removeAllPersons(); */
                hobbyEntity.setPersonsUni(original.getPersons());

                em.getTransaction().begin();
                em.merge(hobbyEntity);
                em.getTransaction().commit();
            }
            return new HobbyDTO(hobbyEntity);
        }
        finally {
            em.close();
        }
    }

    @Override
    public HobbyDTO delete(long id)
    {
        EntityManager em = emf.createEntityManager();
        try {
            Hobby h = em.find(Hobby.class, id);
            if (h != null) {
                em.getTransaction().begin();
                h.removeAllPersons();
                em.remove(h);
                em.getTransaction().commit();
                return new HobbyDTO(h);
            }
            else throw new WebApplicationException("No hobby with id: " + id, 404);
        } finally {
            em.close();
        }
    }

    @Override
    public HobbyDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Hobby hobby = em.find(Hobby.class, id);
            return new HobbyDTO(hobby);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByCategory(String category) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE h.category = :category", Hobby.class);
            query.setParameter("category", category);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByType(String type) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h WHERE h.type = :type", Hobby.class);
            query.setParameter("type", type);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByPerson(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = new Person(personDTO);
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h JOIN Person p WHERE p = :p AND h MEMBER OF p.hobbies", Hobby.class);
            query.setParameter("p", person);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByPhone(PhoneDTO phoneDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Phone phone = new Phone(phoneDTO);
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h JOIN Person p WHERE h MEMBER OF p.hobbies AND :phone MEMBER OF p.phones", Hobby.class);
            query.setParameter("phone", phone);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByZip(ZipDTO zipDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Zip zip = new Zip(zipDTO);
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h JOIN Person p WHERE h MEMBER OF p.hobbies AND p.address.zip = :zip", Hobby.class);
            query.setParameter("zip", zip);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByAddress(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Address address = new Address(addressDTO);
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h JOIN Person p WHERE h MEMBER OF p.hobbies AND p.address = :address", Hobby.class);
            query.setParameter("address", address);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
            List<Hobby> hobbies = query.getResultList();
            return HobbyDTO.getDtos(hobbies);
        } finally {
            em.close();
        }
    }

    @Override
    public long getHobbyCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long hobbyCount = (long) em.createQuery("SELECT COUNT(h) FROM Hobby h").getSingleResult();
            return hobbyCount;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        HobbyFacade hf = getHobbyFacade(emf);
        hf.getAll().forEach(dto -> System.out.println(dto));
    }

}
