package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.*;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.CommitException;
import by.epam.javatraining.webproject.exception.MedicalCardDAOException;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.validation.RegistrationValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = null;
        if (type == ActionType.POST) {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String patronymic = request.getParameter("patronymic");
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String repeatPassword = request.getParameter("repeat_password");
            String sex = request.getParameter("sex");
            String dateOfBirth = request.getParameter("date");

            if (RegistrationValidation.validateNameComponent(name) && RegistrationValidation.validateNameComponent(surname)
                    && RegistrationValidation.validateNameComponent(patronymic) && RegistrationValidation.validateLogin(login)
                    && RegistrationValidation.validatePassword(password) && RegistrationValidation.validateRepeatPassword(repeatPassword, password)) {

                ConnectionPool pool = ConnectionPool.getInstance();
                UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                userDAO.getConnection(pool);

                try {

                    if (userDAO.getByLogin(login) == null) {

                        userDAO.setAutoCommit(false);

                        User user = new User(UserRole.PATIENT, login, password.hashCode(), name, surname, patronymic);

                        if(userDAO.insert(user)) {

                            int userID = userDAO.getByLogin(login).getId();
                            logger.debug("user id from database by login " + login + ": " + userID);
                            user.setId(userID);
                            logger.debug("Created user: " + user.toString());

                            request.getSession().setAttribute("user", user);

                            MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
                            MedicalCard card = new MedicalCard(userID, dateOfBirth, Byte.parseByte(sex));

                            try {
                                cardDAO.getConnection(pool);
                                cardDAO.insert(card);
                                logger.debug("Created card: " + card.toString());
                                request.getSession().setAttribute("card", card);

                            } finally {
                                cardDAO.releaseConnection(pool);
                            }

                            userDAO.commit();
                            userDAO.setAutoCommit(true);
                        }
                    } else {
                        logger.info("User with this e-mail already exists");
                        return Pages.REDIRECT_LOGIN + "&login=" + login;
                    }
                } catch (CommitException | MedicalCardDAOException | UserDAOException e) {
                    logger.error(e.getMessage());
                    userDAO.rollback();
                    request.setAttribute("error_message", Messages.REGISTRATION_FAILED);
                    return null;
                } finally {
                    userDAO.releaseConnection(pool);
                }
                page = Pages.REDIRECT_VIEW_PROFILE;
            }
        } else {
            page = Pages.PATIENT_REGISTRATION;
        }
        return page;
    }
}
