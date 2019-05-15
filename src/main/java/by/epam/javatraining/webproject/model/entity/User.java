package by.epam.javatraining.webproject.model.entity;

import by.epam.javatraining.webproject.model.entity.role.UserRole;

import java.util.Objects;

public class User extends Entity {

    private String login;
    private long password;
    private String name;
    private String surname;
    private String patronymic;
    private UserRole role;

    public User(int id, UserRole role, String login, long password, String name, String surname, String patronymic) {
        super(id);
        this.role = role;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.password = password;
    }

    public User( UserRole role, String login, long password, String name, String surname, String patronymic) {
        super();
        this.role = role;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.password = password;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return password == user.password &&
                Objects.equals(login, user.login) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(patronymic, user.patronymic) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, name, surname, patronymic, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role=" + role +
                ", id=" + id +
                '}';
    }
}
