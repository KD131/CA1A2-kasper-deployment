package facades;

import dtos.AddressDTO;
import dtos.PersonDTO;
import dtos.ZipDTO;
import entities.Address;
import entities.Person;
import entities.Zip;
import facades.inter.ZipFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class ZipFacade implements ZipFacadeInterface {

    private static ZipFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ZipFacade() {
    }

    public static ZipFacade getZipFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ZipFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public ZipDTO create(ZipDTO zip) {
        EntityManager em = emf.createEntityManager();
        try {
            Zip zipEntity = new Zip(zip.getId(), zip.getCity());
            em.getTransaction().begin();
            em.persist(zipEntity);
            em.getTransaction().commit();
            return new ZipDTO(zipEntity);
        } finally {
            em.close();
        }
    }

    @Override
    public ZipDTO edit(ZipDTO Zip) {
        return null;
    }

    @Override
    public void delete(long zip) {

        EntityManager em = emf.createEntityManager();
        try {
            ZipDTO zipDTO = new ZipDTO(em.find(Zip.class, zip));
            if (zipDTO != null) {
                em.remove(zipDTO);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public ZipDTO getByZip(long zip) {
        EntityManager em = emf.createEntityManager();
        try {
            return new ZipDTO(em.find(Zip.class, zip));
        } finally {
            em.close();
        }
    }

    @Override
    public ZipDTO getByPerson(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = new Person(personDTO);
            TypedQuery<Zip> query = em.createQuery("SELECT z FROM Zip z JOIN Person p WHERE p.address.zip = z AND p = :person", Zip.class);
            query.setParameter("person", person);
            Zip zip = query.getSingleResult();
            return new ZipDTO(zip);
        } finally {
            em.close();
        }
    }

    @Override
    public ZipDTO getByAddress(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Address address = new Address(addressDTO);
            TypedQuery<Zip> query = em.createQuery("SELECT z FROM Zip z JOIN Address a WHERE a.zip = z AND a = :address", Zip.class);
            query.setParameter("address", address);
            Zip zip = query.getSingleResult();
            return new ZipDTO(zip);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ZipDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Zip> query = em.createQuery("SELECT z FROM Zip z", Zip.class);
            List<Zip> zips = query.getResultList();
            return ZipDTO.getDtos(zips);
        } finally {
            em.close();
        }
    }

    @Override
    public long getZipCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long zipCount = (long) em.createQuery("SELECT COUNT(z) FROM Zip z").getSingleResult();
            return zipCount;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        ZipFacade zf = getZipFacade(emf);
        zf.getAll().forEach(dto -> System.out.println(dto));
    }
}
