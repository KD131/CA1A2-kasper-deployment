package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.*;
import facades.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("hobby")
public class HobbyResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PhoneFacade PHONE_FACADE = PhoneFacade.getPhoneFacade(EMF);
    private static final PersonFacade PERSON_FACADE = PersonFacade.getPersonFacade(EMF);
    private static final ZipFacade ZIP_FACADE = ZipFacade.getZipFacade(EMF);
    private static final AddressFacade ADDRESS_FACADE = AddressFacade.getAddressFacade(EMF);
    private static final HobbyFacade HOBBY_FACADE = HobbyFacade.getHobbyFacade(EMF);
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
        List<HobbyDTO> hobbies = HOBBY_FACADE.getAll();
        return GSON.toJson(hobbies);
    }

    @Path("zip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByZip(@PathParam("zip") int zip) {
        ZipDTO zipDTO = ZIP_FACADE.getByZip(zip);
        List<HobbyDTO> hobbies = HOBBY_FACADE.getByZip(zipDTO);
       return GSON.toJson(hobbies);
    }

    @Path("address/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByAddress(@PathParam("id") long id) {
       AddressDTO addressDTO = ADDRESS_FACADE.getById(id);
        List<HobbyDTO> hobbies = HOBBY_FACADE.getByAddress(addressDTO);
        return GSON.toJson(hobbies);
    }

    @Path("phone/{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByAddress(@PathParam("number") int number) {
        PhoneDTO phone = PHONE_FACADE.getByNumber(number);
        List<HobbyDTO> hobbies =  HOBBY_FACADE.getByPhone(phone);
        return GSON.toJson(hobbies);
    }

    @Path("person/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByPerson(@PathParam("id") long id) {
        PersonDTO person = PERSON_FACADE.getById(id);
        List<HobbyDTO> hobbies =  HOBBY_FACADE.getByPerson(person);
        return GSON.toJson(hobbies);
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getHobbyCount() {

        long count = HOBBY_FACADE.getHobbyCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }
}
