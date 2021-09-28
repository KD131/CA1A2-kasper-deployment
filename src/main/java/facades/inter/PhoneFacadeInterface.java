package facades.inter;

import dtos.PersonDTO;

import java.util.List;

public interface PhoneFacadeInterface {

    PhoneDTO create(PhoneDTO phone);

    PhoneDTO getById(long id);

    List<PhoneDTO> getAll();

    long getPhoneCount();
}