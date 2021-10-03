package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDTO;
import facades.AddressFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("address")
public class AddressResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final AddressFacade FACADE = AddressFacade.getAddressFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("list")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAll() {
        List<AddressDTO> addresses = FACADE.getAll();
        return GSON.toJson(addresses);
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") long id) {
        AddressDTO addressDTO = FACADE.getById(id);
        return GSON.toJson(addressDTO);
    }

    @Path("zip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("zip") int zip) {
        List<AddressDTO> addressDTOs = FACADE.getByZip(zip);
        return GSON.toJson(addressDTOs);
    }

    @Path("person/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByPersonId(@PathParam("id") long id) {
        List<AddressDTO> addressDTOs = FACADE.getByPerson(id);
        return GSON.toJson(addressDTOs);
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAddressCount() {

        long count = FACADE.getAddressCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateAddress(String address) {
        AddressDTO aDTO = GSON.fromJson(address, AddressDTO.class);
        AddressDTO aNew = FACADE.edit(aDTO);
        return GSON.toJson(aNew);
    }
}
