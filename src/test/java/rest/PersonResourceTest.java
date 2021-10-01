package rest;

import dtos.PersonDTO;
import edu.emory.mathcs.backport.java.util.Arrays;
import entities.*;
import facades.PersonFacade;
import io.restassured.http.ContentType;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
@Disabled

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;
    private static PersonFacade personFacade;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        personFacade = PersonFacade.getPersonFacade(emf);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Person(
                new ArrayList<Phone>(Arrays.asList(new Phone[]{new Phone(11111111)})),
                "bob@bob.com",
                "Bob",
                "Roberts",
                new Address("Test street 21",
                        new Zip(6969, "Nice-ville")));

        List<Phone> phones2 = new ArrayList<>();
        phones2.add(new Phone(22222222));
        p2 = new Person(phones2,
                "alice@alice.com",
                "Alice",
                "Allison",
                new Address("2nd and Hill 34",
                        new Zip(4242, "Cool-town")));

        try {
            em.getTransaction().begin();
            // needs either cascading delete or more delete queries to take out the other tables
//            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Person.resetPK").executeUpdate();
//            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Zip.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void demo() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void getByPhone() {
    }

    @Test
    void getByZip() {
    }

    @Test
    void getPersonCount() {
    }

    @Test
    void getPopulate() {
    }

    @Test
    void updatePerson() {
        PersonDTO p2DTO = new PersonDTO(p2);
        p2DTO.setFirstName("John");

        given()
                .contentType(ContentType.JSON)
                .body(p2DTO)
                .when()
                .put("/person")
                .then()
                .body("email", equalTo("alice@alice.com"))
                .body("firstName", equalTo("John"))
                .body("lastName", equalTo("Allison"))
                .body("address.address", equalTo("2nd and Hill 34"))
                .body("id", equalTo(p2DTO.getId().intValue())); // maybe cast to int or delete...
    }
}