package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SearchCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            String name = request.getParameter(Parameters.NAME);
            User user = (User) request.getSession().getAttribute(Parameters.USER);
            UserRole role = null;
            if (user != null) {
                role = user.getRole();
            }
            UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
            userService.takeConnection();

            List<User> users = null;

            try {
                if (role == UserRole.ADMINISTRATOR) {
                    users = userService.searchByNameAmongCertainUserType(null, name);
                    request.setAttribute(Parameters.USERS, users);
                    page = Pages.VIEW_USERS;

                } else if (role == UserRole.PATIENT || role == null) {
                    users = userService.searchByNameAmongCertainUserType(UserRole.DOCTOR, name);
                    users.addAll(userService.searchByNameAmongCertainUserType(UserRole.NURSE, name));
                    request.setAttribute(Parameters.DOCTORS, users);
                    page = Pages.VIEW_DOCTORS;

                } else if (role == UserRole.DOCTOR || role == UserRole.NURSE) {
                    users = userService.searchByNameAmongCertainUserType(UserRole.PATIENT, name);
                    request.setAttribute(Parameters.PATIENTS, users);
                    page = Pages.FORWARD_VIEW_ALL_PATIENTS;
                }
            } catch (UserServiceException e) {
                logger.error("search failed: " + e.getMessage());
            }
        }
        return page;
    }
}
