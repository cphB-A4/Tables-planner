package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.EventDTO;
import dtos.PersonDTO;
import dtos.TablesDTO;
import entities.Event;
import entities.Person;
import entities.Tables;
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
    public TablesDTO createTable(TablesDTO tablesDTO, String eventId, String username){
        EntityManager em = emf.createEntityManager();
        Event event;
        try {
            event = em.find(Event.class,eventId);

        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find event with eventId: " + eventId);
        }

        //check if user owns the event
        if (event.getUser().getUserName().equals(username)){
            System.out.println("success. user connected to event");
        } else {
            throw new WebApplicationException("You are not authenticated to perform the request");
        }

        Tables table = new Tables(tablesDTO.getSize(), tablesDTO.getShape());
        if ((table.getShape().length() == 0)) {
            throw new WebApplicationException("Have you filled out all the forms?", 400);
        }
        try {
            em.getTransaction().begin();
            event.addTable(table);
            em.merge(event);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage());
        } finally {
            em.close();
        }

        return new TablesDTO(table);
    }

    public List<PersonDTO> getAllPersonsByTable(int tableId) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.tables.id = :tableId", Person.class);
            query.setParameter("tableId", tableId);
            List<Person> persons = query.getResultList();
            List<PersonDTO> personDTOS = new ArrayList<>();
            for (Person currentPerson : persons) {
                personDTOS.add(new PersonDTO(currentPerson));
            }
            return personDTOS;
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), 500);
        } finally {
            em.close();
        }
    }

    public PersonDTO createPerson(PersonDTO personDTO, String tableId, String username){
        EntityManager em = emf.createEntityManager();
        Tables table;
        try {
            table = em.find(Tables.class,Integer.valueOf(tableId));
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find table with id: " + tableId);
        }

        //check to make sure person amount doesn't exceed table size
        if(getAllPersonsByTable(table.getId()).size() < table.getSize()){
            System.out.println("table is suited to get more persons");
        } else {
            throw new WebApplicationException("Cannot add more persons to the table");
        }

        //check if user owns the person
        if (table.getEvent().getUser().getUserName().equals(username)){
            System.out.println("success. user connected to event");
        } else {
            throw new WebApplicationException("You are not authenticated to perform the request");
        }
        Person person = new Person(personDTO.getName());
        if ((person.getName().length() == 0)) {
            throw new WebApplicationException("Have you filled out all the forms?", 400);
        }
        try {
            em.getTransaction().begin();
            table.addPerson(person);
            em.merge(table);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage());
        } finally {
            em.close();
        }

        return new PersonDTO(person);
    }

    public PersonDTO deletePerson(int personId, String username) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        Person person;
        try {
            person = em.find(Person.class,personId);
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find table with id: " + personId);
        }
        //check if user owns the person
        if (person.getTables().getEvent().getUser().getUserName().equals(username)){
            System.out.println("success. user connected to event");
        } else {
            throw new WebApplicationException("You are not authenticated to perform the request");
        }
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            //System.out.println(getAllPersonsByTable(person.getTables().getId()).size());
            return new PersonDTO(person);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new WebApplicationException("Could not delete, provided id: " + personId + " does not exist", 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }
}
