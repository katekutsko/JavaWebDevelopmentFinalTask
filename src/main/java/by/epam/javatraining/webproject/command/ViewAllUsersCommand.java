package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
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
            userService.getConnection();

            List<User> userList = userService.getAll();

            if (userList != null) {
                request.setAttribute("users", userList);
                request.setAttribute("roles", UserRole.values());
                page = Pages.VIEW_USERS;
            } else {
                request.setAttribute("errorMessage", Messages.NO_RESULTS);
            }
            userService.releaseConnection();
        }
        return page;
    }
}
