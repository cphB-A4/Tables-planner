package dtos;

import entities.Person;

public class PersonDTO {
    private String name;
    private Integer id;

    public PersonDTO(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
    public PersonDTO(Person person){
        this.name = person.getName();
        this.id = person.getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
