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
import by.epam.javatraining.webproject.model.validation.UserDataValidation;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_REGISTRATION;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {

            String name = request.getParameter(Parameters.NAME);
            String surname = request.getParameter(Parameters.SURNAME);
            String patronymic = request.getParameter(Parameters.PATRONYMIC);
            String login = request.getParameter(Parameters.LOGIN);
            String password = request.getParameter(Parameters.PASSWORD);
            String repeatPassword = request.getParameter(Parameters.REPEAT_PASSWORD);
            String phoneNumber = request.getParameter(Parameters.PHONE_NUMBER);
            String sex = request.getParameter(Parameters.SEX);
            String dateOfBirth = request.getParameter(Parameters.BIRTH_DATE);
            //String chosenRole = request.getParameter(Parameters.ROLE);

            login = login.trim();
            List<String> errorMessages = UserDataValidation.formRegistrationErrorMessage(name, surname, patronymic, password, repeatPassword, login, phoneNumber);

            if (errorMessages == null) {

                try {
                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.takeConnection();
                    User user = (User) session.getAttribute(Parameters.USER);

                    if (userService.checkLoginUniqueness(user, login)) {

                        String role = request.getParameter(Parameters.ROLE);
                        UserRole userRole = UserRole.PATIENT;

                        if (role != null) {
                            userRole = UserRole.valueOf(role);
                        }

                        User newUser = new User(login, password.hashCode(), name, surname, patronymic, userRole, phoneNumber);
                        userService.setAutoCommit(false);
                        userService.addUser(newUser);

                        if (user == null) {
                            int userId = userService.getIdByLogin(login);

                            MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                            cardService.setConnection(userService.getConnection());
                            MedicalCard card = new MedicalCard(userId, dateOfBirth, Byte.parseByte(sex));

                            if (cardService.addCard(card) && userService.commit()) {
                                int cardId = cardService.getIdByUserId(userId);
                                card.setId(cardId);
                                session.setAttribute(Parameters.CARD, card);
                                session.setAttribute(Parameters.USER, newUser);
                                page = Pages.REDIRECT_VIEW_PROFILE;

                            } else {
                                userService.rollback();
                            }
                        } else {/*
                            if (UserRole.DOCTOR.name().equals(chosenRole)) {
                                String specialisationAsString = request.getParameter(Parameters.SPECIALISATION);
                                String officeNumberAsString = request.getParameter(Parameters.OFFICE_NUMBER);

                                if (specialisationAsString != null && !specialisationAsString.isEmpty()){
                                    Specialisation specialisation = Specialisation.valueOf(specialisationAsString);
                                } else {
                                    errorMessages.add(Messages.FIELDS_NOT_FILLED);
                                }

                                if (RegistrationValidation.validateOfficeNumber(officeNumberAsString)){

                                } else {

                                }
                                Doctor doctor = new Doctor(specialisation,

                            }
                        }*/
                            page = Pages.REDIRECT_VIEW_USERS;
                        }
                        userService.setAutoCommit(true);
                        session.removeAttribute(Parameters.ERRORS);
                        session.removeAttribute(Parameters.R_ERROR);
                    } else {
                        page = Pages.REDIRECT_LOGIN + "login=" + login;
                    }
                    userService.releaseConnection();

                } catch (UserServiceException | MedicalCardServiceException e) {
                    logger.error("could not register: " + e.getMessage());
                    page = Pages.REDIRECT_ERROR_PAGE;
                    session.removeAttribute(Parameters.ERRORS);
                    session.setAttribute(Parameters.R_ERROR, Messages.INTERNAL_ERROR);
                }
            } else {
                logger.debug(errorMessages);
                if (UserDataValidation.lookForEmptyFields(name, surname, patronymic, password, login, phoneNumber, sex, dateOfBirth)) {
                    session.removeAttribute(Parameters.ERRORS);
                    session.setAttribute(Parameters.R_ERROR, Messages.FIELDS_NOT_FILLED);
                } else {
                    session.removeAttribute(Parameters.R_ERROR);
                    session.setAttribute(Parameters.ERRORS, errorMessages);
                }
            }
        } else {
            session.setAttribute(Parameters.ROLES, UserRole.values());
            page = Pages.REGISTRATION;
        }
        return page;
    }
}
