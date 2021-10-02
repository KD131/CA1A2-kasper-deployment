package facades;

import dtos.AddressDTO;
import entities.Address;
import entities.Zip;
import facades.inter.AddressFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class AddressFacade implements AddressFacadeInterface {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {
    }

    @Override
    public AddressDTO edit(Address address) {
        return null;
    }

    public static AddressFacade getAddressFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public AddressDTO create(AddressDTO address) {
        EntityManager em = emf.createEntityManager();
        Zip zipEntity = em.find(Zip.class, address.getZip().getId());
        Address addressEntity = new Address(address.getAddress(), zipEntity);
        try {
            em.getTransaction().begin();
            em.persist(addressEntity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AddressDTO(addressEntity);
    }

    @Override
    public void delete(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            AddressDTO addressDTO = new AddressDTO(em.find(Address.class, id));
            if(addressDTO != null) {
                em.remove(addressDTO);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new AddressDTO(em.find(Address.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<AddressDTO> getByZip() {
        return null;
    }

    @Override
    public long getAddressCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long addressCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
            return addressCount;
        } finally {
            em.close();
        }
    }

    @Override
    public List<AddressDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a", Address.class);
            List<Address> addresses = query.getResultList();
            return AddressDTO.getDtos(addresses);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        AddressFacade af = getAddressFacade(emf);
        af.getAll().forEach(dto -> System.out.println(dto));
    }
}
