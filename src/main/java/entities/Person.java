package entities;

import javax.persistence.*;

@Table(name = "person")
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
        @NamedQuery(name = "Person.getAllRows", query = "SELECT p from Person p"),
        @NamedQuery(name = "Person.getPerson", query = "SELECT p from Person p WHERE p.name = :p")
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    @ManyToOne
    private Tables tables;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tables getTables() {
        return tables;
    }

    public void setTables(Tables tables) {
        this.tables = tables;
    }
}