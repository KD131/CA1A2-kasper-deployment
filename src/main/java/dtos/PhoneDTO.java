/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Hobby;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tha
 */
public class PhoneDTO {
    private long id;
    private int number;
    private String info;

    public PhoneDTO(int number, String info) {
        this.number = number;
        this.info = info;
    }

    public static List<PhoneDTO> getDtos(List<Phone> phones) {
        List<PhoneDTO> PhoneDTOs = new ArrayList();
        phones.forEach(phone -> PhoneDTOs.add(new PhoneDTO(phone)));
        return PhoneDTOs;
    }

    public PhoneDTO(Phone phone) {
        if (phone.getId() != null)
            this.id = phone.getId();
            this.number = phone.getNumber();
            this.info = phone.getInfo();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
