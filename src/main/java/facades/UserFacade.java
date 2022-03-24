package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.EventDTO;
import entities.Event;
import entities.Person;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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


    public EventDTO createEvent(EventDTO eventDTO, String username){
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class,username);
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find user with username: " + username);
        }
        Event event = new Event(eventDTO.getUser(), eventDTO.getDescription(), eventDTO.getTitle(), eventDTO.getTime());
        if ((event.getDescription().length() == 0) || (event.getTitle().length() == 0)) {
            throw new WebApplicationException("Have you filled out all the forms?", 400);
        }
        try {
            Event getEvent = new Event(eventDTO.getUser(), eventDTO.getDescription(), eventDTO.getTitle(), eventDTO.getTime());
            em.getTransaction().begin();
            user.addEvent(getEvent);
            em.merge(user);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage());
        } finally {
            em.close();
        }

        return new EventDTO(event);
    }

    public List<EventDTO> getAllEventsByUsername(String username) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e WHERE e.user.userName = :username", Event.class);
            query.setParameter("username", username);
            List<Event> events = query.getResultList();
            List<EventDTO> eventDTOS = new ArrayList<>();
            for (Event post : events) {
                eventDTOS.add(new EventDTO(post));
            }
            return eventDTOS;
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), 500);
        } finally {
            em.close();
        }
    }
}
