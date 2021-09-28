package facades.inter;

import dtos.PersonDTO;

import java.util.List;

public interface AddressFacadeInterface {

    AddressDTO create(AddressDTO address);

    AddressDTO getById(long id);

    List<AddressDTO> getAll();

    long getAddressCount();
}
