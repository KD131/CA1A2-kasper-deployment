package facades.inter;

import dtos.ZipDTO;

import java.util.List;

public interface ZipFacadeInterface {

    ZipDTO create(ZipDTO Zip);

    ZipDTO edit(ZipDTO Zip);

    boolean delete(int zip);

    ZipDTO getById(int zip);

    List<ZipDTO> getAll();

    long getZipCount();
}
