package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.*;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.Case;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.CaseDAOException;
import by.epam.javatraining.webproject.exception.InvalidMedicalCardException;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SelectPatientCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }


    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        if (type == ActionType.GET) {
            String cardId = request.getParameter("card_id");
            int id = Integer.parseInt(cardId);

            UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
            MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
            CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);

            ConnectionPool pool = ConnectionPool.getInstance();
            cardDAO.getConnection(pool);

            MedicalCard card = (MedicalCard) cardDAO.getById(id);
            cardDAO.releaseConnection(pool);

            if (card != null) {
                try {
                    userDAO.getConnection(pool);
                    User user = (User) userDAO.getById(card.getUserID());
                    userDAO.releaseConnection(pool);

                    caseDAO.getConnection(pool);
                    Case lastCase = caseDAO.getLastCaseByPatientId(id);
                    caseDAO.releaseConnection(pool);

                    request.setAttribute("last_case", lastCase);
                    request.setAttribute("patient", user);
                    request.setAttribute("card", card);

                    return Pages.FORWARD_VIEW_PATIENT;

                } catch (UserDAOException | CaseDAOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return Pages.ERROR_PAGE;
    }
}
