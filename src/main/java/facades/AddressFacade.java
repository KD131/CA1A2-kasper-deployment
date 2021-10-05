package facades;

import dtos.AddressDTO;
import dtos.PersonDTO;
import dtos.ZipDTO;
import entities.Address;
import entities.Person;
import entities.Zip;
import facades.inter.AddressFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;


public class AddressFacade implements AddressFacadeInterface {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {
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
    public AddressDTO create(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        Zip zip = em.find(Zip.class, addressDTO.getZip().getId());
        if (zip == null)
            throw new WebApplicationException("ZIP code " + addressDTO.getZip().getId() + " not found.", 404);
        Address address = new Address(addressDTO.getAddress(), zip);
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            return new AddressDTO(address);
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO update(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Address address = new Address(addressDTO);
            if (addressDTO.getId() == getById(addressDTO.getId()).getId()) {
                em.getTransaction().begin();
                em.merge(address);
                em.getTransaction().commit();
                if (address == null) ;
                return new AddressDTO(address);
            } else throw new WebApplicationException("Address not found", 404);
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO delete(long id) throws Exception {
        EntityManager em = emf.createEntityManager();
        Address address = em.find(Address.class, id);
        try {
            AddressDTO addressDTO = new AddressDTO(address);
            if (getById(id) != null) {
                if (!address.getPersons().isEmpty()) {
                    throw new WebApplicationException("Address has persons.", 400);
                }
                em.getTransaction().begin();
                em.remove(address);
                em.getTransaction().commit();
            }
            return addressDTO;
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO getById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Address address = em.find(Address.class, id);
            if (address == null) throw new WebApplicationException("Address not found", 404);
            return new AddressDTO(address);
        } finally {
            em.close();
        }
    }

    public AddressDTO getByFields(AddressDTO addressDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.address = :address AND a.zip.zip = :zip", Address.class);
            query.setParameter("address", addressDTO.getAddress());
            query.setParameter("zip", addressDTO.getZip().getId());
            Address address = query.getSingleResult();
            if (address == null) throw new WebApplicationException("Address not found.", 404);
            return new AddressDTO(address);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AddressDTO> getByZip(ZipDTO zipDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Zip zip = new Zip(zipDTO);
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.zip = :zip", Address.class);
            query.setParameter("zip", zip);
            List<Address> addresses = query.getResultList();
            if (addresses.size() == 0) throw new WebApplicationException("Addresses not found.", 404);
            return AddressDTO.getDtos(addresses);
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO getByPerson(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = new Person(personDTO);
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a JOIN Person p WHERE a = p.address AND p = :person", Address.class);
            query.setParameter("person", person);
            Address address = query.getSingleResult();
            if (address == null) throw new WebApplicationException("Address not found", 404);
            return new AddressDTO(address);
        } finally {
            em.close();
        }
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
            if (addresses.size() == 0) throw new WebApplicationException("Addresses not found", 404);
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
