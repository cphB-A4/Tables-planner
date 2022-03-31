package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BigEventDTO;
import dtos.EventDTO;
import dtos.PersonDTO;
import dtos.TablesDTO;
import facades.AdminFacade;
import facades.AllFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Path("user")
public class UserResource {
    
    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final static UserFacade userFacade = UserFacade.getUserFacade(EMF);
    private final AllFacade allFacade = AllFacade.getAllFacade(EMF);
    private final AdminFacade adminFacade = AdminFacade.getAdminFacade(EMF);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromuser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (User) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get-all-events-by-user")
    @RolesAllowed("user")
    public String getEventByUser() {
            String thisUser = securityContext.getUserPrincipal().getName();
            List<EventDTO> events = userFacade.getAllEventsByUsername(thisUser);
            return gson.toJson(events);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("event/{id}")
    public String getEventByUser(@PathParam("id") String id) {
        BigEventDTO eventDTO = userFacade.getEventById(id);
        return gson.toJson(eventDTO);
    }

    //
    @POST
    @Path("createEvent")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String createEvent(String jsonEvent) {
        System.out.println(jsonEvent);
        String thisUser;
        try {
            //if event.user_user_name == securityContext.getUserPrincipal().getName();
            thisUser = securityContext.getUserPrincipal().getName();
            EventDTO eventDTO = userFacade.createEvent(gson.fromJson(jsonEvent, EventDTO.class), thisUser);
            return gson.toJson(eventDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }

    @POST
    @Path("createTable/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String createTable(@PathParam("eventId") String eventId, String jsonTable) {
        System.out.println(jsonTable);
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            TablesDTO tablesDTO = userFacade.createTable(gson.fromJson(jsonTable, TablesDTO.class), eventId, thisUser);
            return gson.toJson(tablesDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }

    @POST
    @Path("createPerson/{tableId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String createPerson(@PathParam("tableId") String tableId, String jsonPerson) {
        System.out.println(jsonPerson);
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            PersonDTO personDTO = userFacade.createPerson(gson.fromJson(jsonPerson, PersonDTO.class), tableId, thisUser);
            return gson.toJson(personDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }
    @Path("deletePerson/{id}")
    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePerson(@PathParam("id") int personId) {
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            PersonDTO personDTO = userFacade.deletePerson(personId, thisUser);
            return gson.toJson(personDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }
    @Path("deleteTable/{id}")
    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTable(@PathParam("id") int tableId) {
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            TablesDTO tablesDTO = userFacade.deleteTable(tableId, thisUser);
            return gson.toJson(tablesDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }
    @Path("deleteEvent/{id}")
    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEvent(@PathParam("id") String eventId) {
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            EventDTO eventDTO = userFacade.deleteEvent(eventId, thisUser);
            return gson.toJson(eventDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }
    @Path("/{id}")
    @RolesAllowed("user")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editDog(@PathParam("id") String eventID, String event) {
        try {
            EventDTO eventDTO = gson.fromJson(event, EventDTO.class);
            eventDTO = userFacade.editEvent(eventDTO, eventID);
            return gson.toJson(eventDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage());
        }
    }
}