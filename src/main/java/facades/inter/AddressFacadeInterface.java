package facades.inter;

import dtos.AddressDTO;

import java.util.List;

public interface AddressFacadeInterface {

    AddressDTO create(AddressDTO address);

    AddressDTO edit(AddressDTO addressDTO);

    void delete(long id);

    AddressDTO getById(long id);

    List<AddressDTO> getAll();

    List<AddressDTO> getByZip(int zip);

    List<AddressDTO> getByPerson(long id);

    long getAddressCount();
}
