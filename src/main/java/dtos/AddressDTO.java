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
public class AddressDTO {
    private Long id;
    private String address;

    private Zip zip;

    public AddressDTO(String address, Zip zip) {
        this.address = address;
        this.zip = zip;
    }

    public static List<AddressDTO> getDtos(List<Address> addresses) {
        List<AddressDTO> addressDTOs = new ArrayList();
        addresses.forEach(address -> addressDTOs.add(new AddressDTO(address)));
        return addressDTOs;
    }


    public AddressDTO(Address address) {
        if (address.getId() != null)
            this.id = address.getId();
            this.address = address.getAddress();
            this.zip = address.getZip();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Zip getZip() {
        return zip;
    }

    public void setZip(Zip zip) {
        this.zip = zip;
    }
}

