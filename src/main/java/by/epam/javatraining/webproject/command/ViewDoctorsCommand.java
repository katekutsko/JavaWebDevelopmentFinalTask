package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
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

public class ViewDoctorsCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
        userService.takeConnection();

        List<User> userList = null;
        try {
            userList = userService.getAllOfType(UserRole.DOCTOR);
            userList.addAll(userService.getAllOfType(UserRole.NURSE));

            request.setAttribute(Parameters.DOCTORS, userList);
            request.setAttribute(Parameters.AMOUNT, userList.size());

            logger.info("doctors were found and set as attributes: " + userList);
            logger.info("amount of doctors: " + userList.size());

            page = Pages.VIEW_DOCTORS;

        } catch (UserServiceException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            userService.releaseConnection();
        }
        return page;
    }

}
