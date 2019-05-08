package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.*;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.exception.UserDAOException;
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
                UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                ConnectionPool pool = ConnectionPool.getInstance();

                userDAO.getConnection(pool);

                try {
                    User user  = userDAO.getByLogin(login);
                    if (user != null) {
                        logger.debug("found user: " + user);
                        long passwordHash = user.getPassword();

                        if (passwordHash == password.hashCode()) {
                            logger.debug("successful log in as " + user.getRole());
                            request.getSession().setAttribute("user", user);

                            if (user.getRole() == UserRole.PATIENT){
                                MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
                                cardDAO.getConnection(pool);
                                MedicalCard card = cardDAO.getByPatientId(user.getId());
                                request.getSession().setAttribute("card", card);
                                cardDAO.releaseConnection(pool);
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

                } catch (UserDAOException e) {
                    page = null;
                } finally {
                    userDAO.releaseConnection(pool);
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
