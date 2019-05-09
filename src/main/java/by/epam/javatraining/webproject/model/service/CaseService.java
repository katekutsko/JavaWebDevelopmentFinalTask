package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.CaseDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.exception.CaseDAOException;

import java.util.List;

public class CaseService extends Service {

    private CaseDAO caseDAO;

    {
        caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
        dao = caseDAO;
    }

    public Case getLastCaseByPatientId(int id) {
        Case lastCase = null;
        try {
            lastCase = caseDAO.getLastCaseByPatientId(id);
        } catch (CaseDAOException e) {
            logger.error(e.getMessage());
        }
        return lastCase;
    }

    public List<Case> getAllCasesOfCertainPatient(int cardId) {
        List<Case> caseList = null;
        try {
            caseList = caseDAO.getAllCasesOfCertainPatient(cardId);
        } catch (CaseDAOException e) {
            logger.error(e.getMessage());
        }
        return caseList;
    }
}
