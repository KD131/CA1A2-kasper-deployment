package facades.inter;

import dtos.PersonDTO;

import java.util.List;

public interface ZipFacadeInterface {

    ZipDTO create(ZipDTO Zip);

    ZipDTO getById(long id);

    List<ZipDTO> getAll();

    long getZipCount();
}
