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
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = null;

        if (type == ActionType.POST) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            logger.debug("login: " + login + " password: " + password);

            if (!login.equals("") && !password.equals("")) {

                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.getConnection();
                User user = userService.login(login);
                userService.releaseConnection();

                if (user != null) {
                    logger.debug("found user: " + user);
                    long passwordHash = user.getPassword();

                    if (passwordHash == password.hashCode()) {
                        logger.debug("successful log in as " + user.getRole());
                        request.getSession().setAttribute("user", user);

                        if (user.getRole() == UserRole.PATIENT) {
                            MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                            cardService.getConnection();
                            MedicalCard card = cardService.getByPatientId(user.getId());
                            cardService.releaseConnection();
                            request.getSession().setAttribute("card", card);
                        }
                        page = Pages.REDIRECT_VIEW_PROFILE;
                    } else {
                        logger.info("wrong password");
                        request.removeAttribute("loginError");
                        request.setAttribute("passwordError", "You entered wrong password");
                    }
                } else {
                    logger.info("wrong login");
                    request.removeAttribute("passwordError");
                    request.setAttribute("loginError", "You entered wrong login");
                }
            } else {
                request.setAttribute("loginError", "Fields were not filled");
            }
        } else {
            request.removeAttribute("loginError");
            request.removeAttribute("passwordError");
        }
        return page;
    }
}
