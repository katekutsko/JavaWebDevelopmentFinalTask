package by.epam.javatraining.webproject.model.entity;

public class Entity {

    protected int id;

    protected Entity(int id) {
        this.id = id;
    }

    protected Entity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
