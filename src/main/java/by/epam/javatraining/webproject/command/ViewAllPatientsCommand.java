package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.CaseDAO;
import by.epam.javatraining.webproject.model.dao.implementation.MedicalCardDAO;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.exception.CaseDAOException;
import by.epam.javatraining.webproject.model.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ViewAllPatientsCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
        MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
        CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);

        ConnectionPool pool = ConnectionPool.getInstance();

            try {
                userDAO.getConnection(pool);
                cardDAO.getConnection(pool);

                List<User> userList = userDAO.getAllOfType(UserRole.PATIENT);
                List<MedicalCard> cardList = cardDAO.getAll();
                List<Case> lastCasesList = new ArrayList<>();

                if (userList != null && cardList != null) {
                    caseDAO.getConnection(pool);

                    for (MedicalCard card : cardList) {

                        try {
                            lastCasesList.add(caseDAO.getLastCaseByPatientId(card.getId()));
                            logger.debug("something was added to lastcase list");
                            request.setAttribute("patients", userList);
                            request.setAttribute("cards", cardList);
                            request.setAttribute("cases", lastCasesList);
                            request.setAttribute("amount", userList.size());

                            logger.info("patients were found and set as attributes");
                            logger.info("amount of patients: " + userList.size());
                        } catch (CaseDAOException e) {
                            logger.warn("bad medical card ID " + card.getId());
                        }
                    }
                } else {
                    logger.warn("patients were not found");
                }

                return Pages.FORWARD_VIEW_ALL_PATIENTS;

            } catch (UserDAOException e) {
                logger.warn("couldn't extract users from database. cause: " + e.getMessage());
            } finally {
                userDAO.releaseConnection(pool);
                cardDAO.releaseConnection(pool);
                caseDAO.releaseConnection(pool);
            }
        return Pages.ERROR_PAGE;
    }

}
