package by.epam.javatraining.webproject.model.entity;

import by.epam.javatraining.webproject.model.entity.role.UserRole;

import java.util.Objects;

public class User extends Entity {

    private String login;
    private int password;
    private String name;
    private String surname;
    private String patronymic;
    private UserRole role;
    private String phoneNumber;
    private boolean blocked;

    public User(int id, String login, int password, String name, String surname, String patronymic, UserRole role, String phoneNumber) {
        super(id);
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.blocked = false;
    }

    public User(String login, int password, String name, String surname, String patronymic, UserRole role, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.blocked = false;
    }

    public User() {
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return password == user.password &&
                login.equals(user.login) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                patronymic.equals(user.patronymic) &&
                role == user.role &&
                phoneNumber.equals(user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, name, surname, patronymic, role, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password=" + password +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role=" + role +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id=" + id +
                ", blocked=" + blocked +
                '}';
    }
}
