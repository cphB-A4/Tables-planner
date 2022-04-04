package rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.BigEventDTO;
import facades.AdminFacade;
import facades.AllFacade;
import facades.SvgFacade;
import facades.UserFacade;
import svg.TablePlanSVG;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Locale;
@Path("svg")
public class SVGResource {
    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final static SvgFacade svgFacade = SvgFacade.getSvgFacade(EMF);
    private final static UserFacade userFacade = UserFacade.getUserFacade(EMF);
    private final AllFacade allFacade = AllFacade.getAllFacade(EMF);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("generatesvg/{id}")
    public static String tablesPlanAsSvg(@PathParam("id") String id){
        Locale.setDefault(new Locale("US")); //laver , om til . (svg arbejder ik med komma )
        BigEventDTO  bigEventDTO = userFacade.getEventById(id);
        TablePlanSVG tablePlanSVG = new TablePlanSVG(0,0, "0 0 1000 900",100,100, bigEventDTO);
        //TablePlanSVG tablePlanSVG = svgFacade.tablesPlanAsSvg(bigEventDTO);
        String svg = tablePlanSVG.genarateSvg();
        return svg;
        //"{\"msg\": \"Hello to (User) User: " + thisuser + "\"}";
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("event/{id}")
    public String getEventByUser(@javax.ws.rs.PathParam("id") String id) {
        BigEventDTO eventDTO = userFacade.getEventById(id);
        return gson.toJson(eventDTO);
    }
}
