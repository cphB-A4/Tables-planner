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


    public String getId() {
        return id;
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