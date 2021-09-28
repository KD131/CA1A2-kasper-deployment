package facades.inter;

import dtos.HobbyDTO;
import dtos.PersonDTO;

import java.util.List;

public interface HobbyFacadeInterface {

    HobbyDTO create(HobbyDTO Hobby);

    HobbyDTO edit(HobbyDTO Hobby);

    boolean delete(long id);

    HobbyDTO getById(long id);

    List<HobbyDTO> getByCategory(String category);

    List<HobbyDTO> getIfOutdoor(boolean outdoor);

    List<HobbyDTO> getByPerson(PersonDTO person);

    List<HobbyDTO> getByZip(ZipDTO Zip);

    List<HobbyDTO> getAll();

    long getHobbyCount();
}
