package facades;

import dtos.AddressDTO;

import dtos.PersonDTO;
import dtos.ZipDTO;
import entities.Address;
import entities.Person;
import entities.Zip;
import errorhandling.ExceptionDTO;
import facades.inter.AddressFacadeInterface;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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
    public AddressDTO create(AddressDTO address) {
        EntityManager em = emf.createEntityManager();
        Zip zipEntity = em.find(Zip.class, address.getZip().getId());
        Address addressEntity = new Address(address.getAddress(), zipEntity);
        try {
            em.getTransaction().begin();
            em.persist(addressEntity);
            em.getTransaction().commit();
            return new AddressDTO(addressEntity);
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
                return new AddressDTO(address);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public AddressDTO delete(long id) throws Exception
    {
        EntityManager em = emf.createEntityManager();
        Address a = em.find(Address.class, id);
        try {
            AddressDTO aDTO = new AddressDTO(a);
            if(getById(id) != null) {
                if (!a.getPersons().isEmpty()) {
                    throw new Exception("Address has persons.");
                }
                em.getTransaction().begin();
                em.remove(a);
                em.getTransaction().commit();
            }
            return aDTO;
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
    public List<AddressDTO> getByZip(ZipDTO zipDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Zip zip = new Zip(zipDTO);
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.zip = :zip", Address.class);
            query.setParameter("zip", zip);
            List<Address> addresses = query.getResultList();
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
