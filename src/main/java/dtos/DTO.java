package dtos;

abstract public class DTO {
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean hasId() {
        return id != 0;
    }
}
