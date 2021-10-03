package facades.inter;

import dtos.PersonDTO;
import dtos.PhoneDTO;

import java.util.List;

public interface PhoneFacadeInterface {

    PhoneDTO create(PhoneDTO phone);

    PhoneDTO edit(PhoneDTO phoneDTO);

    void delete(long id);

    PhoneDTO getById(long id);

    public PhoneDTO getByNumber(int number);

    List<PhoneDTO> getByPerson(PersonDTO person);

    List<PhoneDTO> getAll();

    long getPhoneCount();
}
