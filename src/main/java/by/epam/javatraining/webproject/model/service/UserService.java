package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.exception.DAOException;
import by.epam.javatraining.webproject.model.exception.UserDAOException;

import java.util.List;

public class UserService extends Service {

    UserDAO userDAO;

    {
        userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
        dao = userDAO;
    }

    public User login(String login) {

        try {
            return userDAO.getByLogin(login);
        } catch (UserDAOException e) {
            return null;
        }
    }

    public boolean addUser(User newUser) {
       boolean result = false;

        try {
            userDAO.insert(newUser);
            result = true;
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            result = false;
        }
        return result;
    }

    public int getIdByLogin(String login) {
        User user = login(login);
        int id = 0;
        if (user != null){
            id = user.getId();
        }
        return id;
    }

    public List<User> getAllOfType(UserRole role) {
        List<User> userList = null;
        try {
            userList = userDAO.getAllOfType(role);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
        }
        return  userList;
    }


    public List<User> getAll() {
        List<User> entities = null;
        try {
            entities = dao.getAll();
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return entities;
    }
}
