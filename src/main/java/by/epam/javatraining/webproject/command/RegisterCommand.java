package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.model.validation.RegistrationValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RegisterCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

      /*String page = null;
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
        return page;*/

        String page = Pages.ERROR_PAGE;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {

            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String patronymic = request.getParameter("patronymic");
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String repeatPassword = request.getParameter("repeat_password");

            if (RegistrationValidation.validateNameComponent(name) && RegistrationValidation.validateNameComponent(surname)
                    && RegistrationValidation.validateNameComponent(patronymic) && RegistrationValidation.validateLogin(login)
                    && RegistrationValidation.validatePassword(password) &&
                    RegistrationValidation.validateRepeatPassword(repeatPassword, password)) {

                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.getConnection();

                User user = (User) session.getAttribute("user");

                if (userService.login(login) == null) {

                    String role = request.getParameter("role");
                    UserRole userRole = UserRole.PATIENT;

                    if (role != null) {
                        userRole = UserRole.valueOf(role);
                    }

                    User newUser = new User(userRole, login, password.hashCode(), name, surname, patronymic);

                    if (userService.setAutoCommit(false)) {

                        if (userService.addUser(newUser)) {

                            if (user == null) {
                                String sex = request.getParameter("sex");
                                String dateOfBirth = request.getParameter("date");

                                int userId = userService.getIdByLogin(login);
                                MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                                cardService.getConnection();
                                MedicalCard card = new MedicalCard(userId, dateOfBirth, Byte.parseByte(sex));

                                if (cardService.addCard(card) && userService.commit()) {
                                    userService.setAutoCommit(true);
                                    userService.releaseConnection();
                                    int cardId = cardService.getIdByUserId(userId);

                                    if (cardId != 0) {
                                        card.setId(cardId);
                                        session.setAttribute("card", card);
                                        session.setAttribute("user", user);
                                        page = Pages.REDIRECT_VIEW_PROFILE;
                                    }
                                } else {
                                    userService.rollback();
                                    userService.releaseConnection();
                                }
                            } else {
                                userService.setAutoCommit(true);
                                userService.releaseConnection();
                                page = Pages.REDIRECT_VIEW_USERS;
                            }
                        }
                    }
                } else {
                    return Pages.REDIRECT_LOGIN + "&login=" + login;
                }
            }
        } else {
            session.setAttribute("roles", UserRole.values());
            page = Pages.REGISTRATION;
        }
        return page;
    }
}
