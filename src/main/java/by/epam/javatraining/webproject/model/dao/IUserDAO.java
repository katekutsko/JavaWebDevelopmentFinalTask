package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.dao.exception.UserDAOException;

import java.util.List;

public interface IUserDAO extends IDAO {

    User getByLogin(String login) throws UserDAOException;
    boolean setBlocking(int id, boolean blocked) throws UserDAOException;
}
