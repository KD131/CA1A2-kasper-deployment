package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import dtos.ZipDTO;
import entities.Person;
import facades.PersonFacade;
import facades.PhoneFacade;
import facades.PopulatorPerson;
import facades.ZipFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private final PersonFacade PERSON_FACADE = PersonFacade.getPersonFacade(EMF);
    private final PhoneFacade PHONE_FACADE = PhoneFacade.getPhoneFacade(EMF);
    private final ZipFacade ZIP_FACADE = ZipFacade.getZipFacade(EMF);
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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

    @Path("populate")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPopulate() {
        String pop = PopulatorPerson.populate();
        return "{\"Message:\":" + pop + "}";
    }

    @PUT
    @Path("id/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePerson(@PathParam("id") long id,  String person) {
        PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
        pDTO.setId(id);
        PersonDTO pNew = PERSON_FACADE.edit(pDTO);
        return GSON.toJson(pNew);
    }
    
}
