package facades;

import dtos.PhoneDTO;
import entities.Phone;
import facades.inter.PhoneFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PhoneFacade implements PhoneFacadeInterface {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhoneFacade() {
    }

    public static PhoneFacade getPhoneFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PhoneDTO create(PhoneDTO phone) {
        Phone phoneEntity = new Phone(phone.getNumber(), phone.getInfo());
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(phoneEntity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDTO(phoneEntity);
    }

    @Override
    public PhoneDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        return new PhoneDTO(em.find(Phone.class, id));
    }

    @Override
    public long getPhoneCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long phoneCount = (long) em.createQuery("SELECT COUNT(p) FROM Phone p").getSingleResult();
            return phoneCount;
        } finally {
            em.close();
        }
    }

    @Override
    public List<PhoneDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p", Phone.class);
        List<Phone> phones = query.getResultList();
        return PhoneDTO.getDtos(phones);
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PhoneFacade pf = getPhoneFacade(emf);
        pf.getAll().forEach(dto -> System.out.println(dto));
    }
}
