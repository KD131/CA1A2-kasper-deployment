package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import dtos.ZipDTO;
import facades.PersonFacade;
import facades.PhoneFacade;
import facades.ZipFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade PERSON_FACADE = PersonFacade.getPersonFacade(EMF);
    private static final PhoneFacade PHONE_FACADE = PhoneFacade.getPhoneFacade(EMF);
    private static final ZipFacade ZIP_FACADE = ZipFacade.getZipFacade(EMF);
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
        List<PersonDTO> persons = PERSON_FACADE.getAll();
        return GSON.toJson(persons);
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") long id) {
        PersonDTO person = PERSON_FACADE.getById(id);
        return GSON.toJson(person);
    }

    @Path("phone/{number}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByPhone(@PathParam("number") int number) {
        PhoneDTO phone = PHONE_FACADE.getByNumber(number);
        PersonDTO person = PERSON_FACADE.getByPhone(phone);
        return GSON.toJson(person);
    }

    @Path("zip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getByZip(@PathParam("zip") int zipNumber) {
        ZipDTO zip = ZIP_FACADE.getByZip(zipNumber);
        List<PersonDTO> persons = PERSON_FACADE.getByZip(zip);
        return GSON.toJson(persons);
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {

        long count = PERSON_FACADE.getPersonCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }
}
