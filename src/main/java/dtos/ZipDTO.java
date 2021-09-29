/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Address;
import entities.Person;
import entities.Zip;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class ZipDTO {
    private int zip;
    private String city;

    public ZipDTO(int zip, String city) {
        this.zip = zip;
        this.city = city;
    }

    public static List<ZipDTO> getDtos(List<Zip> zips) {
        List<ZipDTO> ZipDTOs = new ArrayList();
        zips.forEach(zip -> ZipDTOs.add(new ZipDTO(zip)));
        return ZipDTOs;
    }


    public ZipDTO(Zip zip) {
        //if (zip.getZip() != null) int can't be compared to nul with != operator
            this.zip = zip.getZip();
            this.city = zip.getCity();

    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

