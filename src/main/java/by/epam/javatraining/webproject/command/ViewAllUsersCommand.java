package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

public class ViewAllUsersCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
            userService.takeConnection();

            List<User> userList = null;

            try {
                userList = userService.getAll();
                if (userList != null) {
                    request.setAttribute(Parameters.USERS, userList);
                    request.setAttribute(Parameters.ROLES, UserRole.values());
                    page = Pages.VIEW_USERS;
                } else {
                    request.setAttribute(Parameters.ERROR, Messages.NO_RESULTS);
                }
            } catch (UserServiceException e) {
                logger.error(e.getMessage());
                request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            } finally {
                userService.releaseConnection();
            }
        }
        return page;
    }
}
