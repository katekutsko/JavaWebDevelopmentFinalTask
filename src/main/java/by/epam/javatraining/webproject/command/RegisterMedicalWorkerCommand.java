package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.UserDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.validation.RegistrationValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class RegisterMedicalWorkerCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            request.setAttribute("roles", UserRole.values());
            page = Pages.MEDICAL_WORKER_REGISTRATION;

        } else {
            String role = request.getParameter("role");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String patronymic = request.getParameter("patronymic");
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String repeatPassword = request.getParameter("repeat_password");

            if (RegistrationValidation.validateNameComponent(name) && RegistrationValidation.validateNameComponent(surname)
                    && RegistrationValidation.validateNameComponent(patronymic) && RegistrationValidation.validateLogin(login)
                    && RegistrationValidation.validatePassword(password) && RegistrationValidation.validateRepeatPassword(repeatPassword, password)) {

                ConnectionPool pool = ConnectionPool.getInstance();
                UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                userDAO.getConnection(pool);

                try {

                    if (userDAO.getByLogin(login) == null) {
                        UserRole userRole = UserRole.valueOf(role.toUpperCase());
                        User user = new User(userRole, login, password.hashCode(), name, surname, patronymic);

                        if (userDAO.insert(user)) {
                            int userID = userDAO.getByLogin(login).getId();
                            logger.debug("user id from database by login " + login + ": " + userID);
                            user.setId(userID);
                            logger.debug("Created medical worker: " + user.toString());
                        }

                    } else {
                        logger.info("User with this e-mail already exists");
                        request.setAttribute("error_message", Messages.REGISTRATION_FAILED);
                    }
                } catch (UserDAOException e) {
                    logger.error(e.getMessage());
                    request.setAttribute("error_message", Messages.REGISTRATION_FAILED);
                } finally {
                    userDAO.releaseConnection(pool);
                }
                page = Pages.REDIRECT_VIEW_PROFILE;
            }
        }
        return page;
    }
}
