package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.CaseDAO;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.PrescriptionDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.Case;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.Prescription;
import by.epam.javatraining.webproject.exception.CaseDAOException;
import by.epam.javatraining.webproject.exception.PrescriptionDAOException;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ViewPrescriptionsCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;
        MedicalCard card = (MedicalCard) request.getSession().getAttribute("card");

        if (card != null) {

            logger.debug("card id is " + card.getId());
            ConnectionPool pool = ConnectionPool.getInstance();
            CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
            caseDAO.getConnection(pool);
            PrescriptionDAO prescriptionDAO = (PrescriptionDAO) DAOFactory.getDAO(DAOType.PRESCRIPTION_DAO);
            prescriptionDAO.getConnection(pool);

            try {
                Case lastCase = caseDAO.getLastCaseByPatientId(card.getId());

                if (lastCase != null) {
                    List<Prescription> prescriptionList = prescriptionDAO.getAllByCaseId(lastCase.getId());

                    if (prescriptionList != null && !prescriptionList.isEmpty()) {
                        request.setAttribute("prescriptions", prescriptionList);
                    } else {
                        request.setAttribute("message", Messages.NO_RESULTS);
                    }
                } else {
                    request.setAttribute("message", Messages.NO_RESULTS);
                }
                page = Pages.VIEW_PRESCRIPTIONS;
            } catch (CaseDAOException | PrescriptionDAOException e) {
                logger.error(e.getMessage());
            } finally {
                caseDAO.releaseConnection(pool);
                prescriptionDAO.releaseConnection(pool);
            }
        }
        return page;
    }
}
