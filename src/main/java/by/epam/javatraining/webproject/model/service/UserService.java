package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.exception.DAOException;
import by.epam.javatraining.webproject.model.exception.UserDAOException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;

import java.util.*;

public class UserService extends Service {

    private UserDAO userDAO;

    {
        userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
        dao = userDAO;
    }

    public User login(String login) throws UserServiceException {

        try {
            return userDAO.getByLogin(login);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
    }

    public boolean addUser(User newUser) throws UserServiceException {
        boolean result = false;

        try {
            userDAO.insert(newUser);
            result = true;
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
        return result;
    }

    public int getIdByLogin(String login) throws UserServiceException {
        User user = login(login);
        int id = 0;
        if (user != null) {
            id = user.getId();
        }
        return id;
    }

    public List<User> getAllOfType(UserRole role) throws UserServiceException {
        List<User> userList = null;
        try {
            userList = userDAO.getAllOfType(role);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
        return userList;
    }


    public List<User> getAll() throws UserServiceException {
        List<User> entities = null;
        try {
            entities = dao.getAll();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
        return entities;
    }

    public List<User> searchByNameAmongCertainUserType(UserRole role, String name) throws UserServiceException {

        List<User> users = null;

        if (role != null) {
            users = getAllOfType(role);
        } else {
            users = getAll();
        }
        List<User> foundUsers = new ArrayList<>();

        if (users != null && name != null && !name.isEmpty()) {
            String[] nameParts = name.split("\\s");

            for (User user : users) {
                String patientName = user.getName();
                String patientSurname = user.getSurname();
                String patientPatronymic = user.getPatronymic();

                byte count = 0;

                for (String namePart : nameParts) {
                    if (namePart.equals(patientName) || namePart.equals(patientSurname) || namePart.equals(patientPatronymic)) {
                        count++;
                    }
                }
                if (count >= 1) {
                    foundUsers.add(user);
                }
            }
        }
        return foundUsers;
    }
}
