package dtos;

import entities.Person;
import entities.Tables;

import java.util.List;

public class TablesDTO {
    private Integer id;
    private int size;
    private String shape;
    private List<PersonDTO> persons;

    public TablesDTO(int size, String shape, List<PersonDTO> persons) {
        this.size = size;
        this.shape = shape;
        this.persons = persons;
    }

    public TablesDTO(Tables tables){
        this.id = tables.getId();
        this.size = tables.getSize();
        this.shape = tables.getShape();
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
}
