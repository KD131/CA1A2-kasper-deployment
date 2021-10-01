package facades.inter;

import dtos.ZipDTO;

import java.util.List;

public interface ZipFacadeInterface {

    ZipDTO create(ZipDTO Zip);

    ZipDTO edit(ZipDTO Zip);

    void delete(int zip);

    ZipDTO getByZip(int zip);

    List<ZipDTO> getAll();

    long getZipCount();
}
