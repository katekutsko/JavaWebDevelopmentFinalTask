package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.dao.exception.DAOException;
import by.epam.javatraining.webproject.model.dao.exception.UserDAOException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;

import java.util.*;

public class UserService extends Service {

    private UserDAO userDAO;

    {
        userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
        dao = userDAO;
    }

    public boolean checkLoginUniqueness(User sessionUser, String login) {
        boolean result = false;
        try {
            User user = login(login);
            if (user == null || user.equals(sessionUser)) {
                logger.debug("comparing users: " + user + " " + sessionUser);
                result = true;
            }
        } catch (UserServiceException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean setBlocking(User user) throws UserServiceException {
        try {
            if (user != null) {
                logger.debug("blocking user: " + user);
                user.setBlocked(!user.getBlocked());
                return userDAO.setBlocking(user.getId(), user.getBlocked());
            }
            logger.debug("user was null");
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
         return false;
    }

    public User login(String login) throws UserServiceException {

        try {
            return userDAO.getByLogin(login);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
    }

    public String getNameByID(int id) throws UserServiceException {

        User user = null;
        try {
            user = (User) userDAO.getById(id);
            String name = null;
            if (user != null) {
                name = user.getSurname() + " " + user.getName() + " " + user.getPatronymic();
            }
            return name;
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException();
        }

    }

    public boolean addUser(User newUser) throws UserServiceException {

        try {
            return userDAO.insert(newUser);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
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

        try {
            return userDAO.getAllOfType(role);
        } catch (UserDAOException e) {
            logger.error(e.getMessage());
            throw new UserServiceException(e.getMessage());
        }
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
                    if (namePart.equalsIgnoreCase(patientName) || namePart.equalsIgnoreCase(patientSurname)
                            || namePart.equalsIgnoreCase(patientPatronymic)) {
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
