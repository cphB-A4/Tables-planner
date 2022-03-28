package dtos;

import entities.Event;
import entities.Person;
import entities.Tables;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class BigEventDTO {
    private String id;
    private String description;
    private String title;
    private String time;
    User user;
    private List<TablesDTO> tablesList;

    public BigEventDTO(User user, String description, String title, String time) {
        this.user = user;
        this.description = description;
        this.title = title;
        this.time = time;
    }

    public List<TablesDTO> getTablesList() {
        return tablesList;
    }

    public void setTablesList(List<TablesDTO> tablesList) {
        this.tablesList = tablesList;
    }



    public BigEventDTO(Event event, List<TablesDTO> tables){
        this.id = event.getId();
        //this.user = event.getUser(); // No need for this --> StackOverflowError
        this.description = event.getDescription();
        this.title = event.getTitle();
        this.time = event.getTime();
        this.tablesList = tables;

    }



    public User getUser() {
        return user;
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
