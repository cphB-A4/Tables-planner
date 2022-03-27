package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "event")
@Entity
@NamedQueries({
        @NamedQuery(name = "Event.deleteAllRows", query = "DELETE from Event"),
        @NamedQuery(name = "Event.getAllRows", query = "SELECT e from Event e"),
        @NamedQuery(name = "Event.getEvent", query = "SELECT e from Event e WHERE e.title = :e")
})
public class Event {
    @Id
    private String id;

    private String description;
    private String title;
    private String time;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<Tables> tablesList;

    @ManyToOne
    private User user;//ændring

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event(User user, String description, String title, String time) {
        this.user = user;//ændring
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.title = title;
        this.time = time;
        this.tablesList = new ArrayList<>();
    }
    public Event(String description, String title, String time) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.title = title;
        this.time = time;
        this.tablesList = new ArrayList<>();
    }

    public void addTable(Tables table) {
        this.tablesList.add(table);
        if (table != null){
            table.setEvent(this);
        }
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Event() {
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}