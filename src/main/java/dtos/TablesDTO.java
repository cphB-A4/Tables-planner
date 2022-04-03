package dtos;

import entities.Person;
import entities.Tables;

import java.util.ArrayList;
import java.util.List;

public class TablesDTO {
    private Integer id;
    private int size;
    private String shape;
    private List<PersonDTO> persons;

    public TablesDTO(Integer id, int size, String shape, List<PersonDTO> persons) {
        this.id = id;
        this.size = size;
        this.shape = shape;
        this.persons = persons;
    }

    public TablesDTO(Tables tables){
        this.id = tables.getId();
        this.size = tables.getSize();
        this.shape = tables.getShape();
        this.persons = tables.getPersonList() != null || tables.getPersonList().size() > 0 ? makeDTOlist(tables.getPersonList()) : new ArrayList<>();
    }

    private List<PersonDTO> makeDTOlist(List<Person> personList) {
        List<PersonDTO> personDTOS = new ArrayList<>();
        for (Person person : personList) {
            personDTOS.add(new PersonDTO(person));
        }
        return personDTOS;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}
