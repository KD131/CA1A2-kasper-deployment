package facades.inter;

import dtos.PersonDTO;

import java.util.List;

public interface PersonFacadeInterface {

    PersonDTO create(PersonDTO person);

    PersonDTO getById(long id);

    List<PersonDTO> getAll();

    long getPersonCount();
}
