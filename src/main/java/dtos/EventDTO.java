package dtos;

import entities.Event;

import java.util.UUID;

public class EventDTO {

    private String id;
    private String description;
    private String title;
    private String time;

    public EventDTO(String description, String title, String time) {
        this.description = description;
        this.title = title;
        this.time = time;
    }

    public EventDTO(Event event){
        this.id = event.getId();
        this.description = event.getDescription();
        this.title = event.getTitle();
        this.time = event.getTime();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
