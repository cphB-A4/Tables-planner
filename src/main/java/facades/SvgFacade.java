package facades;

import dtos.BigEventDTO;
import dtos.RenameMeDTO;
import entities.RenameMe;
import svg.TablePlanSVG;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;


public class SvgFacade {

    private static SvgFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private SvgFacade() {}

    public static SvgFacade getSvgFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SvgFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    public String tablesPlanAsSvg(BigEventDTO bigEventDTO){
        TablePlanSVG tablePlanSVG = new TablePlanSVG(0,0, "0 0 1000 900",150,100, bigEventDTO);

        return "";
    }

}
