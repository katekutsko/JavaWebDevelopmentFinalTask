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

public class RegisterCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {

            String name = request.getParameter(Parameters.NAME);
            String surname = request.getParameter(Parameters.SURNAME);
            String patronymic = request.getParameter(Parameters.PATRONYMIC);
            String login = request.getParameter(Parameters.LOGIN);
            String password = request.getParameter(Parameters.PASSWORD);
            String repeatPassword = request.getParameter(Parameters.REPEAT_PASSWORD);

            if (RegistrationValidation.validateNameComponent(name) && RegistrationValidation.validateNameComponent(surname)
                    && RegistrationValidation.validateNameComponent(patronymic) && RegistrationValidation.validateLogin(login)
                    && RegistrationValidation.validatePassword(password) &&
                    RegistrationValidation.validateRepeatPassword(repeatPassword, password)) {

                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.takeConnection();
                User user = (User) session.getAttribute(Parameters.USER);

                try {
                    login = login.trim();
                    if (userService.login(login) == null) {

                        String role = request.getParameter(Parameters.ROLE);
                        UserRole userRole = UserRole.PATIENT;

                        if (role != null) {
                            userRole = UserRole.valueOf(role);
                        }

                        User newUser = new User(userRole, login, password.hashCode(), name, surname, patronymic);
                        userService.setAutoCommit(false);

                        if (userService.addUser(newUser)) {

                            if (user == null) {
                                String sex = request.getParameter(Parameters.SEX);
                                String dateOfBirth = request.getParameter(Parameters.DATE);
                                user = userService.login(login);
                                int userId = user.getId();

                                MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                                cardService.setConnection(userService.getConnection());
                                MedicalCard card = new MedicalCard(userId, dateOfBirth, Byte.parseByte(sex));

                                if (cardService.addCard(card) && userService.commit()) {
                                    int cardId = cardService.getIdByUserId(userId);

                                    if (cardId != 0) {
                                        card.setId(cardId);
                                        session.setAttribute(Parameters.CARD, card);
                                        session.setAttribute(Parameters.USER, user);
                                    }
                                } else {
                                    userService.rollback();
                                }
                            }
                            userService.setAutoCommit(true);
                            userService.releaseConnection();
                            page = Pages.REDIRECT_VIEW_PROFILE;
                        }
                    } else {
                        return Pages.REDIRECT_LOGIN + "&" + Parameters.LOGIN + "=" + login;
                    }
                } catch (UserServiceException | MedicalCardServiceException e) {
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                }
            } else {
                session.setAttribute(Parameters.ERROR, Messages.INVALID_DATA);
            }
        } else {
            session.setAttribute(Parameters.ROLES, UserRole.values());
            page = Pages.REGISTRATION;
        }
        return page;
    }
}
