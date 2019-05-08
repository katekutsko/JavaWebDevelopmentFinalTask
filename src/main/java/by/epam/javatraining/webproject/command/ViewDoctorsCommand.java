package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.UserDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Messages;
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

        String page = Pages.ERROR_PAGE;
        UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
        ConnectionPool pool = ConnectionPool.getInstance();

        try {
            userDAO.getConnection(pool);

            List<User> userList = userDAO.getAllOfType(UserRole.DOCTOR);

            request.setAttribute("doctors", userList);
            request.setAttribute("amount", userList.size());

            logger.info("doctors were found and set as attributes: " + userList);
            logger.info("amount of doctors: " + userList.size());

            page = Pages.VIEW_DOCTORS;

        } catch (UserDAOException e) {
            request.setAttribute("errorMessage", Messages.getString(Messages.CONNECTION_ERROR));
            logger.warn("couldn't extract users from database. cause: " + e.getMessage());
        } finally {
            userDAO.releaseConnection(pool);
        }
        return page;
    }

}
