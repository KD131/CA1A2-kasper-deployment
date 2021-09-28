/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Hobby;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class HobbyDTO {
    private long id;
    private String name;
    private String link;
    private String category;
    private boolean outdoor;

    public HobbyDTO(String name, String link, String category, boolean outdoor) {
        this.name = name;
        this.link = link;
        this.category = category;
        this.outdoor = outdoor;
    }

    public static List<HobbyDTO> getDtos(List<Hobby> hobbies) {
        List<HobbyDTO> hobbyDTOs = new ArrayList();
        hobbies.forEach(hobby -> hobbyDTOs.add(new HobbyDTO(hobby)));
        return hobbyDTOs;
    }

    public HobbyDTO(Hobby hobby) {
        if (hobby.getId() != null)
            this.id = hobby.getId();
        this.name = hobby.getName();
        this.link = hobby.getLink();
        this.category = hobby.getCategory();
        this.outdoor = hobby.isOutdoor();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }
}
