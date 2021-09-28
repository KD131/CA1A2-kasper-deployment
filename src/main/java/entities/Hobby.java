package entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQuery(name = "Hobby.deleteAllRows", query = "DELETE from Hobby")
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String category;
    private boolean outdoor;

    public Hobby() {
    }

    public Hobby(String name, String link, String category, boolean outdoor) {
        this.name = name;
        this.link = link;
        this.category = category;
        this.outdoor = outdoor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }
}
