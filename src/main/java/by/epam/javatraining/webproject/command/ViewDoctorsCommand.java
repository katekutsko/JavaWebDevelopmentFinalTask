package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
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

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
        userService.getConnection();

        userService.getConnection();

        List<User> userList = userService.getAllOfType(UserRole.DOCTOR);
        userList.addAll(userService.getAllOfType(UserRole.NURSE));

        userService.releaseConnection();
        request.setAttribute("doctors", userList);
        request.setAttribute("amount", userList.size());

        logger.info("doctors were found and set as attributes: " + userList);
        logger.info("amount of doctors: " + userList.size());

        return Pages.VIEW_DOCTORS;
    }

}
