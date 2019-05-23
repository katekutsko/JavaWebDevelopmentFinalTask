package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.model.validation.RegistrationValidation;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements Command {

    private final static int VALIDATION_PARAMETERS_AMOUNT = 4;
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_REGISTRATION;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {

            session.removeAttribute(Parameters.ERROR);

            String name = request.getParameter(Parameters.NAME);
            String surname = request.getParameter(Parameters.SURNAME);
            String patronymic = request.getParameter(Parameters.PATRONYMIC);
            String login = request.getParameter(Parameters.LOGIN);
            String password = request.getParameter(Parameters.PASSWORD);
            String repeatPassword = request.getParameter(Parameters.REPEAT_PASSWORD);

            login = login.trim();
            List<String> errorMessages = formErrorMessage(name, surname, patronymic, password, repeatPassword, login);

            if (errorMessages.isEmpty()) {

                try {
                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.takeConnection();
                    User user = (User) session.getAttribute(Parameters.USER);

                    if (userService.login(login) == null) {

                        String role = request.getParameter(Parameters.ROLE);
                        UserRole userRole = UserRole.PATIENT;

                        if (role != null) {
                            userRole = UserRole.valueOf(role);
                        }

                        User newUser = new User(userRole, login, password.hashCode(), name, surname, patronymic);
                        userService.addUser(newUser);

                        if (user == null) {
                            userService.setAutoCommit(false);

                            String sex = request.getParameter(Parameters.SEX);
                            String dateOfBirth = request.getParameter(Parameters.DATE);
                            user = userService.login(login);
                            int userId = user.getId();

                            MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                            cardService.setConnection(userService.getConnection());
                            MedicalCard card = new MedicalCard(userId, dateOfBirth, Byte.parseByte(sex));

                            if (cardService.addCard(card) && userService.commit()) {
                                int cardId = cardService.getIdByUserId(userId);
                                card.setId(cardId);
                                session.setAttribute(Parameters.CARD, card);
                                session.setAttribute(Parameters.USER, user);
                                page = Pages.REDIRECT_VIEW_PROFILE;

                            } else {
                                userService.rollback();
                            }
                            userService.setAutoCommit(true);
                        } else {
                            page = Pages.REDIRECT_VIEW_USERS;
                            session.removeAttribute(Parameters.ERRORS);
                            session.removeAttribute(Parameters.ERROR);
                        }
                    }
                    userService.releaseConnection();
                } catch (UserServiceException | MedicalCardServiceException e) {
                    logger.error("could not get message: " + e.getMessage());
                    page = Pages.REDIRECT_ERROR_PAGE;
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                }
            } else {
                session.setAttribute(Parameters.ERRORS, errorMessages);
            }
        } else {
            session.setAttribute(Parameters.ROLES, UserRole.values());
            page = Pages.REGISTRATION;
        }
        return page;
    }

    private List<String> formErrorMessage(String name, String surname, String patronymic, String password,
                                          String repeatPassword, String login) {

        boolean[] check = new boolean[VALIDATION_PARAMETERS_AMOUNT];
        check[0] = (RegistrationValidation.validateNameComponent(name)
                && RegistrationValidation.validateNameComponent(surname) && RegistrationValidation.validateNameComponent(patronymic));
        check[1] = RegistrationValidation.validatePassword(password);
        check[2] = RegistrationValidation.validateRepeatPassword(repeatPassword, password);
        check[3] = RegistrationValidation.validateLogin(login);

        String[] errorMessages = new String[4];
        errorMessages[0] = Messages.INVALID_NAME;
        errorMessages[1] = Messages.INVALID_PASSWORD;
        errorMessages[2] = Messages.INCONSISTENT_PASSWORDS;
        errorMessages[3] = Messages.INVALID_LOGIN;

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < VALIDATION_PARAMETERS_AMOUNT; i++) {
            if (!check[i]) {
                logger.debug(i + " validation parameter was invalid");
                errors.add(errorMessages[i]);
            } else {
                errors.add(null);
            }
        }
        if (errors != null) {
            logger.debug(errors);
        }
        return errors;
    }
}
