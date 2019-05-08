package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.MedicalCardDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ViewProfileCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = null;
        if (type == ActionType.GET) {
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                if (user.getRole() == UserRole.PATIENT) {
                    MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
                    ConnectionPool pool = ConnectionPool.getInstance();
                    cardDAO.getConnection(pool);
                    MedicalCard card = cardDAO.getByPatientId(user.getId());
                    request.getSession().setAttribute("medical_card", card);
                    cardDAO.releaseConnection(pool);
                }
                page = Pages.FORWARD_VIEW_PROFILE;
            }
        }
        return page;
    }
}
