package entities;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "event")
@Entity
public class Event {
    @Id
    private String id;

    private String description;
    private String title;

    public String getId() {
        return id;
    }

    public Event(String description, String title) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.title = title;
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