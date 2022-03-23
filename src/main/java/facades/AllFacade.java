package facades;
import javax.persistence.EntityManagerFactory;

public class AllFacade {

    private static EntityManagerFactory emf;
    private static AllFacade instance;

    private AllFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AllFacade getAllFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AllFacade();
        }
        return instance;
    }


}
