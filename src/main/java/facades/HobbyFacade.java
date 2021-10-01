package facades;

import dtos.AddressDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.ZipDTO;
import entities.Address;
import entities.Hobby;
import facades.inter.HobbyFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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
    public HobbyDTO create(HobbyDTO hobby) {
        Hobby hobbyEntity = new Hobby(hobby.getName(), hobby.getLink(), hobby.getCategory(), hobby.getType());
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
    public HobbyDTO edit(HobbyDTO Hobby) {
        return null;
    }

    @Override
    public void delete(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            HobbyDTO hobbyDTO = new HobbyDTO(em.find(Hobby.class, id));
            if(hobbyDTO != null) {
                em.remove(hobbyDTO);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public HobbyDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new HobbyDTO(em.find(Hobby.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<HobbyDTO> getByCategory(String category) {
        return null;
    }

    @Override
    public List<HobbyDTO> getByType(String type) {
        return null;
    }

    @Override
    public List<HobbyDTO> getByPerson(PersonDTO person) {
        return null;
    }

    @Override
    public List<HobbyDTO> getByZip(ZipDTO Zip) {
        return null;
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
