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
}
