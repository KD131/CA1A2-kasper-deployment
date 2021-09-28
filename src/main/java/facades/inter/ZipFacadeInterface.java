package facades.inter;

import dtos.ZipDTO;

import java.util.List;

public interface ZipFacadeInterface {

    ZipDTO create(ZipDTO Zip);

    ZipDTO edit(ZipDTO Zip);

    boolean delete(long id);

    ZipDTO getById(long id);

    List<ZipDTO> getAll();

    long getZipCount();
}
