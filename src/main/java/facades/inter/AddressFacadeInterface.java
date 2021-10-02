package facades.inter;

import dtos.AddressDTO;
import entities.Address;

import java.util.List;

public interface AddressFacadeInterface {

    AddressDTO create(AddressDTO address);

    AddressDTO edit(AddressDTO addressDTO);

    void delete(long id);

    AddressDTO getById(long id);

    List<AddressDTO> getAll();

    List<AddressDTO> getByZip();

    long getAddressCount();
}
