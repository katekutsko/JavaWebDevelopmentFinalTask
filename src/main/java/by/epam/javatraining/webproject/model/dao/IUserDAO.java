package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.exception.UserDAOException;

import java.util.List;

public interface IUserDAO extends IDAO {

    List<User> findByLastName(String lastName);
    User findByFullName(String lastName, String firstName, String patronymic);
    User getByLogin(String login) throws UserDAOException;
}
