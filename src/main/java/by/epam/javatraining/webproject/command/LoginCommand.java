package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = null;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {
            String login = request.getParameter(Parameters.LOGIN);
            String password = request.getParameter(Parameters.PASSWORD);
            logger.debug("login: " + login + " password: " + password);

            if (!login.equals("") && !password.equals("")) {
                login = login.trim();
                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.takeConnection();
                User user = null;
                try {
                    user = userService.login(login);

                    if (user != null) {
                        logger.debug("found user: " + user);
                        long passwordHash = user.getPassword();

                        if (passwordHash == password.hashCode()) {
                            logger.debug("successful log in as " + user.getRole());
                            session.setAttribute(Parameters.USER, user);

                            if (user.getRole() == UserRole.PATIENT) {
                                MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                                cardService.setConnection(userService.getConnection());
                                MedicalCard card = cardService.getByPatientId(user.getId());
                                session.setAttribute(Parameters.CARD, card);
                            }
                            page = Pages.REDIRECT_VIEW_PROFILE;
                        } else {
                            logger.info("wrong password");
                            request.removeAttribute(Parameters.LOGIN_ERROR);
                            request.setAttribute(Parameters.PASSWORD_ERROR, Messages.WRONG_PASSWORD);
                        }
                    } else {
                        logger.info("wrong login");
                        request.removeAttribute(Parameters.LOGIN_ERROR);
                        request.setAttribute(Parameters.LOGIN_ERROR, Messages.WRONG_LOGIN);
                    }
                } catch (UserServiceException | MedicalCardServiceException e) {
                    page = Pages.ERROR_PAGE;
                    logger.error(e.getMessage());
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                } finally {
                    userService.releaseConnection();
                }
            } else {
                request.setAttribute(Parameters.LOGIN_ERROR, Messages.FIELDS_NOT_FILLED);
            }
        } else {
            request.removeAttribute(Parameters.LOGIN_ERROR);
            request.removeAttribute(Parameters.PASSWORD_ERROR);
        }
        return page;
    }
}
