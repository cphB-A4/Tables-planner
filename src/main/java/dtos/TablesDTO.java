package dtos;

import entities.Tables;

public class TablesDTO {
    private Integer id;
    private int size;
    private String shape;

    public TablesDTO(Integer id, int size, String shape) {
        this.id = id;
        this.size = size;
        this.shape = shape;
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
