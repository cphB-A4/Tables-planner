package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tables")
@Entity
@NamedQueries({
        @NamedQuery(name = "Tables.deleteAllRows", query = "DELETE from Tables"),
        @NamedQuery(name = "Tables.getAllRows", query = "SELECT t from Tables t"),
        @NamedQuery(name = "Tables.getPerson", query = "SELECT t from Tables t WHERE t.shape = :t")
})
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private int size;
    private String shape;
    @ManyToOne
    private Event event;

    @OneToMany(mappedBy = "tables", cascade = CascadeType.PERSIST)
    private List<Person> personList;

    public Tables(int size, String shape) {
        this.size = size;
        this.shape = shape;
        this.personList = new ArrayList<>();
    }

    public Tables() {
    }

    public void addPerson(Person person) {
        this.personList.add(person);
        if (person != null){
            person.setTables(this);
        }
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}