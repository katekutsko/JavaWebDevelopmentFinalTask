package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class BlockUserCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }


    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = Pages.REDIRECT_ERROR_PAGE;

        try {
            String userId = request.getParameter(Parameters.USER_ID);
            logger.debug("id of user being blocked/unblocked: " + userId);
            UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
            userService.takeConnection();
            User user = (User) userService.getById(Integer.parseInt(userId));

            if (userService.setBlocking(user)) {
                logger.debug("blocking set to " + user.getBlocked());
                page = Pages.REDIRECT_VIEW_USERS;
            } else {
                request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            }
            userService.releaseConnection();
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        }

        return page;
    }
}
