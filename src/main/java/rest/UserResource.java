package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.EventDTO;
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

    //
    @POST
    @Path("createEvent")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String createEvent(String jsonEvent) {
        System.out.println(jsonEvent);
        String thisUser;
        try {
            thisUser = securityContext.getUserPrincipal().getName();
            EventDTO eventDTO = gson.fromJson(jsonEvent, EventDTO.class);
            EventDTO newEventDTO = userFacade.createEvent(eventDTO, thisUser);
            return gson.toJson(newEventDTO);
        } catch (WebApplicationException ex) {
            throw new WebApplicationException(ex.getMessage(), ex.getResponse().getStatus());
        }
    }



}