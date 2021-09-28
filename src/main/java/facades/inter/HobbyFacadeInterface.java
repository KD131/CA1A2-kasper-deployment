package facades.inter;

import dtos.HobbyDTO;

import java.util.List;

public interface HobbyFacadeInterface {
    public HobbyDTO create(HobbyDTO Hobby);
    public HobbyDTO getById(long id);
    public List<HobbyDTO> getAll();
    public long getHobbyCount();
}
