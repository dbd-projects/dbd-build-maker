package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name="characters")
public class Character extends Model {
    // Ebean Finder utility
    public final static Finder<Long, Character> find = new Finder<>(Character.class);

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private CharacterType type;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    public Character(final CharacterType type, final String name, final String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
