package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.EventDTO;
import entities.Event;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }


    public EventDTO createEvent(EventDTO eventDTO){
        EntityManager em = emf.createEntityManager();
        Event event = new Event(eventDTO.getDescription(), eventDTO.getTitle(), eventDTO.getTime());
        if ((event.getDescription().length() == 0) || (event.getTitle().length() == 0)) {
            throw new WebApplicationException("Have you filled out all the forms?", 400);
        }
        em.getTransaction().begin();
        em.persist(event);
        em.getTransaction().commit();

        return new EventDTO(event);
    }

}
