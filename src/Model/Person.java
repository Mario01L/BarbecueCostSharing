package Model;

/**
 * Represents a person involved in the event.
 */
public class Person {
    private int id;
    private String name;

    /**
     * Constructs a Person object.
     *
     * @param id   the ID of the person
     * @param name the name of the person
     */
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

