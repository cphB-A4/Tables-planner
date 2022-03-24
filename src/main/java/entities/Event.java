package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Table(name = "event")
@Entity
public class Event {
    @Id
    private String id;

    private String description;
    private String title;
    private String time;

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
    }
    public Event(String description, String title, String time) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.title = title;
        this.time = time;
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