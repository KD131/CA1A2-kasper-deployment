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
 *
 * @author tha
 */
public class HobbyDTO {
    private long id;
    private String str1;
    private String str2;

    public HobbyDTO(String dummyStr1, String dummyStr2) {
        this.str1 = dummyStr1;
        this.str2 = dummyStr2;
    }

    public static List<HobbyDTO> getDtos(List<Hobby> hobbys){
        List<HobbyDTO> hobbyDTOs = new ArrayList();
        hobbys.forEach(hobby->hobbyDTOs.add(new HobbyDTO(hobby)));
        return hobbyDTOs;
    }


    public HobbyDTO(Hobby hobby) {
        if(hobby.getId() != null)
            this.id = hobby.getId();
        this.str1 = hobby.getDummyStr1();
        this.str2 = hobby.getDummyStr2();
    }

    public String getDummyStr1() {
        return str1;
    }

    public void setDummyStr1(String dummyStr1) {
        this.str1 = dummyStr1;
    }

    public String getDummyStr2() {
        return str2;
    }

    public void setDummyStr2(String dummyStr2) {
        this.str2 = dummyStr2;
    }

    @Override
    public String toString() {
        return "HobbyDTO{" + "id=" + id + ", str1=" + str1 + ", str2=" + str2 + '}';
    }
    
    
    
    
    
    
}
