package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.model.validation.RegistrationValidation;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class EditProfileCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();

        if (type == ActionType.GET) {
            page = Pages.FORWARD_EDIT_PROFILE;

        } else {
            User user = (User) session.getAttribute(Parameters.USER);

            if (user != null) {

                String name = request.getParameter(Parameters.NAME);
                String surname = request.getParameter(Parameters.SURNAME);
                String patronymic = request.getParameter(Parameters.PATRONYMIC);
                String login = request.getParameter(Parameters.LOGIN);
                String oldPassword = request.getParameter(Parameters.OLD_PASSWORD);
                String newPassword = request.getParameter(Parameters.NEW_PASSWORD);
                String repeatNewPassword = request.getParameter(Parameters.REPEAT_NEW_PASSWORD);

                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.takeConnection();

                if (RegistrationValidation.validateNameComponent(name) &&
                        RegistrationValidation.validateNameComponent(surname) &&
                        RegistrationValidation.validateNameComponent(patronymic) &&
                        RegistrationValidation.validateLogin(login)) {

                    if (RegistrationValidation.checkLoginUniqueness(user, userService, login)) {
                        user.setName(name);
                        user.setSurname(surname);
                        user.setPatronymic(patronymic);
                        user.setLogin(login);

                        if (!oldPassword.equals("") && !newPassword.equals("") && !repeatNewPassword.equals("")) {

                            if (oldPassword.hashCode() == user.getPassword()) {

                                if (RegistrationValidation.validatePassword(newPassword) && RegistrationValidation.validateRepeatPassword(newPassword,
                                        repeatNewPassword)) {
                                    user.setPassword(newPassword.hashCode());

                                    if (updateUser(userService, request, user)) {
                                        page = Pages.REDIRECT_VIEW_PROFILE;

                                    } else {
                                        session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                                    }
                                } else {
                                    session.setAttribute(Parameters.ERROR, Messages.INCORRECT_PASSWORD);
                                }
                            } else {
                                session.setAttribute(Parameters.ERROR, Messages.INCORRECT_PASSWORD);
                            }
                        } else if (oldPassword.equals("") && newPassword.equals("") && repeatNewPassword.equals("")) {
                            if (updateUser(userService, request, user)) {
                                page = Pages.REDIRECT_VIEW_PROFILE;
                            } else {
                                session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                            }
                        } else {
                            session.setAttribute(Parameters.ERROR, Messages.INCORRECT_PASSWORD);
                        }
                    } else {
                        session.setAttribute(Parameters.ERROR, Messages.LOGIN_EXISTS);
                    }
                } else {
                    session.setAttribute(Parameters.ERROR, Messages.INVALID_DATA);
                }


            }
        }
        return page;
    }

    private boolean updateUser(UserService userService, HttpServletRequest request, User user) {

        boolean result = false;
        HttpSession session = request.getSession();
        userService.setAutoCommit(false);

        try {

            if (userService.update(user)) {
                logger.info("user successfully updated: " + user);

                if (user.getRole() == UserRole.PATIENT) {
                    String dateOfBirth = request.getParameter(Parameters.BIRTH_DATE);
                    String sex = request.getParameter(Parameters.SEX);

                    MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                    cardService.setConnection(userService.getConnection());
                    MedicalCard card = null;

                    try {
                        card = cardService.getByPatientId(user.getId());
                        if (card != null) {
                            card.setDateOfBirth(dateOfBirth);
                            card.setSex(Byte.parseByte(sex));

                            if (cardService.update(card)) {
                                session.removeAttribute(Parameters.USER);
                                session.removeAttribute(Parameters.CARD);
                                session.setAttribute(Parameters.USER, user);
                                session.setAttribute(Parameters.CARD, card);
                                result = true;
                                logger.info("card updated: " + card);
                            } else {
                                userService.rollback();
                            }
                        }
                    } catch (MedicalCardServiceException e) {
                        logger.error(e.getMessage());
                        session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    }
                } else {
                    session.removeAttribute(Parameters.USER);
                    session.setAttribute(Parameters.USER, user);
                    result = true;
                }
            } else {
                session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
        }
        userService.setAutoCommit(true);
        userService.releaseConnection();
        return result;
    }
}
