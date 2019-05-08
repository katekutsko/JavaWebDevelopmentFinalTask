package by.epam.javatraining.webproject.dao;

import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.UserDAOException;

import java.util.List;

public interface IUserDAO extends IDAO {

    List<User> findByLastName(String lastName);
    User findByFullName(String lastName, String firstName, String patronymic);
    User getByLogin(String login) throws UserDAOException;
}
