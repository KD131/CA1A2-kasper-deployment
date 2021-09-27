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
    private String url;
    private String category;
    private String environment;

    public HobbyDTO(String name, String url, String category, String environment) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.environment = environment;
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
        this.url = hobby.getUrl();
        this.category = hobby.getCategory();
        this.environment = hobby.getEnvironment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
