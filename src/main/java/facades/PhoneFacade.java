package facades;

import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Person;
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
        EntityManager em = emf.createEntityManager();
        try {
            Phone phoneEntity = new Phone(phone.getNumber(), phone.getInfo());
            em.getTransaction().begin();
            em.persist(phoneEntity);
            em.getTransaction().commit();
            return new PhoneDTO(phoneEntity);
        } finally {
            em.close();
        }
    }

    @Override
    public PhoneDTO edit(PhoneDTO phoneDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Phone phone = new Phone(phoneDTO);
            if (phoneDTO.getId() == getById(phoneDTO.getId()).getId()) {
                em.getTransaction().begin();
                em.merge(phone);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
            return phoneDTO;
        }
    }

    @Override
    public PhoneDTO delete(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Phone p = em.find(Phone.class, id);
            PhoneDTO pDTO = new PhoneDTO(p);
            if(getById(id) != null) {
                em.getTransaction().begin();
                Person person = em.createQuery("SELECT p FROM Person p WHERE :phone MEMBER OF p.phones", Person.class)
                        .setParameter("phone", p)
                        .getSingleResult();
                person.removePhone(p);
                em.remove(p);
                em.getTransaction().commit();
            }
            return pDTO;
        } finally {
            em.close();
        }
    }

    @Override
    public PhoneDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new PhoneDTO(em.find(Phone.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public PhoneDTO getByNumber(int number) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE p.number = :number", Phone.class);
            query.setParameter("number", number);
            Phone phone = query.getSingleResult();
            return new PhoneDTO(phone);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PhoneDTO> getByPerson(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        try {
            /* requires bidirectionality, I think
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE p.person.id = :personId", Phone.class);
            */
            // unidirectional from Person to their Phones
            TypedQuery<Phone> query = em.createQuery("SELECT pe.phones FROM Person pe WHERE pe.id = :personId", Phone.class);
            query.setParameter("personId", person.getId());
            List<Phone> phones = query.getResultList();
            return PhoneDTO.getDtos(phones);
        } finally {
            em.close();
        }
    }

    public List<PhoneDTO> getByPersonId(long personId) {
        EntityManager em = emf.createEntityManager();
        try {
            /* requires bidirectionality, I think
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE p.person.id = :personId", Phone.class);
            */
            // unidirectional from Person to their Phones
            TypedQuery<Phone> query = em.createQuery("SELECT pe.phones FROM Person pe WHERE pe.id = :personId", Phone.class);
            query.setParameter("personId", personId);
            List<Phone> phones = query.getResultList();
            return PhoneDTO.getDtos(phones);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PhoneDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p", Phone.class);
            List<Phone> phones = query.getResultList();
            return PhoneDTO.getDtos(phones);
        } finally {
            em.close();
        }
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

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PhoneFacade pf = getPhoneFacade(emf);
        pf.getAll().forEach(dto -> System.out.println(dto));
    }
}
