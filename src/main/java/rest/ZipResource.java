package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ZipDTO;
import facades.ZipFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("zip")
public class ZipResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final ZipFacade FACADE = ZipFacade.getZipFacade(EMF);
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
        List<ZipDTO> zips = FACADE.getAll();
        return GSON.toJson(zips);
    }

    @Path("{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("zip") int zip) {
        ZipDTO zipDTO = FACADE.getByZip(zip);
        return GSON.toJson(zipDTO);
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getZipCount() {

        long count = FACADE.getZipCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }
}
