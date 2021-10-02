package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import facades.PhoneFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("phone")
public class PhoneResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PhoneFacade FACADE = PhoneFacade.getPhoneFacade(EMF);
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
        List<PhoneDTO> phones = FACADE.getAll();
        return GSON.toJson(phones);
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") long id) {
        PhoneDTO phone = FACADE.getById(id);
        return GSON.toJson(phone);
    }

    @Path("{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByPhone(@PathParam("number") int number) {
        PhoneDTO phone = FACADE.getByNumber(number);
        return GSON.toJson(phone);
    }

    @Path("person/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByPhone(@PathParam("id") long personId) {
        List<PhoneDTO> phones = FACADE.getByPersonId(personId);
        return GSON.toJson(phones);
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPhoneCount() {
        long count = FACADE.getPhoneCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePhone(String phone) {
        PhoneDTO pDTO = GSON.fromJson(phone, PhoneDTO.class);
        PhoneDTO pNew = FACADE.edit(pDTO);
        return GSON.toJson(pNew);
    }
}
