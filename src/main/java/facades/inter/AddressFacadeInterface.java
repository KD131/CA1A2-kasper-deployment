package facades.inter;

import dtos.AddressDTO;

import java.util.List;

public interface AddressFacadeInterface {

    AddressDTO create(AddressDTO address);

    AddressDTO edit(AddressDTO address);

    boolean delete(long id);

    AddressDTO getById(long id);

    List<AddressDTO> getAll();

    List<AddressDTO> getByZip();

    long getAddressCount();
}
