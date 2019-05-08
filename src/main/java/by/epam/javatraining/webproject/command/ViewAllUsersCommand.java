package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.UserDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.sun.deploy.config.JREInfo.getAll;

public class ViewAllUsersCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET){
            UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
            ConnectionPool pool = ConnectionPool.getInstance();
            userDAO.getConnection(pool);

            try {
                List<User> userList = userDAO.getAll();
                if (userList != null) {
                    request.setAttribute("users", userList);
                    request.setAttribute("roles", UserRole.values());
                    page = Pages.VIEW_USERS;
                } else {
                    request.setAttribute("errorMessage", Messages.NO_RESULTS);
                }
            } catch (UserDAOException e) {
               logger.error("users weren't extracted: " + e.getMessage());
            } finally {
                userDAO.releaseConnection(pool);
            }
        }
        return page;
    }
}
