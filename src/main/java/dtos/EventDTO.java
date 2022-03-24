package dtos;

import entities.Event;

import java.util.UUID;

public class EventDTO {

    private String id;
    private String description;
    private String title;

    public EventDTO(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public EventDTO(Event event){
        this.id = event.getId();
        this.description = event.getDescription();
        this.title = event.getTitle();
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
