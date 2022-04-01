package facades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.BigEventDTO;
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

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

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
        Event getEvent;
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
            getEvent = new Event(eventDTO.getUser(), eventDTO.getDescription(), eventDTO.getTitle(), eventDTO.getTime());
            em.getTransaction().begin();
            user.addEvent(getEvent);
            em.merge(user);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage());
        } finally {
            em.close();
        }

        return new EventDTO(getEvent);
    }

    public BigEventDTO getEventById(String eventId) throws WebApplicationException{
        EntityManager em = emf.createEntityManager();
        try {
            Event event = em.find(Event.class, eventId);
            List <TablesDTO> tablesDTOSList  = getAllTablessByEvent(event.getId());
            List<TablesDTO> tablesList = new ArrayList<>();
            List<PersonDTO> personDTOS = new ArrayList<>();
            if (tablesDTOSList.size() == 0 || tablesDTOSList == null){
//for frontend to work. Might delete later. (Need to access person array)
                tablesList.add(new TablesDTO(-1,-1,"",personDTOS));
            }
            for (TablesDTO tables:tablesDTOSList
                 ) {
                personDTOS = getAllPersonsByTable(tables.getId());
                tablesList.add(new TablesDTO(tables.getId(),tables.getSize(), tables.getShape(), personDTOS));
            }

            BigEventDTO eventDTO = new BigEventDTO(event, tablesList);


            return eventDTO;
        } catch (NullPointerException ex) {
            throw new WebApplicationException("No event with provided id: " + eventId, 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
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
            System.out.println("success. user connected to table");
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

    public List<TablesDTO> getAllTablessByEvent(String eventId) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Tables> query = em.createQuery("SELECT t FROM Tables t WHERE t.event.id = :eventId", Tables.class);
            query.setParameter("eventId", eventId);
            List<Tables> tablesList = query.getResultList();
            List<TablesDTO> tablesDTOS = new ArrayList<>();
            for (Tables tables : tablesList) {
                tablesDTOS.add(new TablesDTO(tables));
            }
            return tablesDTOS;
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex.getMessage(), 500);
        } finally {
            em.close();
        }
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
            System.out.println("success. user connected to person");
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
            throw new WebApplicationException("Could not find person with id: " + personId);
        }
        //check if user owns the person
        if (person.getTables().getEvent().getUser().getUserName().equals(username)){
            System.out.println("success. user connected to person");
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

    public TablesDTO deleteTable(int tableId, String username) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        Tables table;
        //List<PersonDTO> personDTOS = getAllPersonsByTable(tableId);
        try {
            table = em.find(Tables.class,tableId);
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find table with id: " + tableId);
        }
        //check if user owns the person
        if (table.getEvent().getUser().getUserName().equals(username)){
            System.out.println("success. user connected to table");
        } else {
            throw new WebApplicationException("You are not authenticated to perform the request");
        }
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.tables.id = :tableId", Person.class);
        query.setParameter("tableId", tableId);
        List<Person> persons = query.getResultList();
        try {
            em.getTransaction().begin();
            /*for (PersonDTO personDTO:personDTOS) {
            Person person = new Person(personDTO.getName());
            person.setId(personDTO.getId());
            em.remove(person);
        }*/
            for (Person person:persons) {
                em.remove(person);
            }
            em.remove(table);
            em.getTransaction().commit();
            //System.out.println(getAllPersonsByTable(person.getTables().getId()).size());
            return new TablesDTO(table);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new WebApplicationException("Could not delete, provided id: " + tableId + " does not exist", 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public EventDTO deleteEvent(String eventId, String username) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        Event event;
        try {
            event = em.find(Event.class,eventId);
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find event with id: " + eventId);
        }
        //check if user owns the person
        if (event.getUser().getUserName().equals(username)){
            System.out.println("success. user connected to event");
        } else {
            throw new WebApplicationException("You are not authenticated to perform the request");
        }
        TypedQuery<Tables> query = em.createQuery("SELECT t FROM Tables t WHERE t.event.id = :eventId", Tables.class);
        query.setParameter("eventId", eventId);
        List<Tables> tablesList = query.getResultList();
        try {
            em.getTransaction().begin();
            for (Tables table:tablesList) {
                TypedQuery<Person> personQuery = em.createQuery("SELECT p FROM Person p WHERE p.tables.id = :tableId", Person.class);
                personQuery.setParameter("tableId", table.getId());
                List<Person> persons = personQuery.getResultList();
                for (Person person: persons) {
                    em.remove(person);
                }
                em.remove(table);
            }
            em.remove(event);
            em.getTransaction().commit();

            return new EventDTO(event);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new WebApplicationException("Could not delete, provided id: " + eventId + " does not exist", 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public EventDTO editEvent(EventDTO eventDTO, String eventID) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();

        Event getEvent = em.find(Event.class, eventID);
        try {
            em.getTransaction().begin();

            getEvent.setDescription(eventDTO.getDescription());
            getEvent.setTitle(eventDTO.getTitle());
            getEvent.setTime(eventDTO.getTime());

            em.merge(getEvent);
            em.getTransaction().commit();
            return new EventDTO(getEvent);

        } catch (RuntimeException ex) {
            throw new WebApplicationException("Something went wrong", 500);
        }
        finally {
            em.close();
        }

    }
}

