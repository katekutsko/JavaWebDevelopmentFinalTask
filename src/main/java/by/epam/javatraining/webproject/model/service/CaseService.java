package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.CaseDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.dao.exception.CaseDAOException;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;

import java.util.List;

public class CaseService extends Service {

    private CaseDAO caseDAO;

    {
        caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
        dao = caseDAO;
    }

    public Case getLastCaseByPatientId(int id) throws CaseServiceException {
        Case lastCase = null;
        try {
            lastCase = caseDAO.getLastCaseByPatientId(id);
        } catch (CaseDAOException e) {
            logger.error(e.getMessage());
            throw new CaseServiceException(e.getMessage());
        }
        return lastCase;
    }

    public List<Case> getAllCasesOfCertainPatient(int cardId) throws CaseServiceException {

        try {
            return caseDAO.getAllCasesOfCertainPatient(cardId);
        } catch (CaseDAOException e) {
            logger.error(e.getMessage());
            throw new CaseServiceException(e.getMessage());
        }
    }

    public boolean closeCase(Case lastCase) throws CaseServiceException {

        try {
            return caseDAO.closeCase(lastCase);
        } catch (CaseDAOException e) {
            logger.error(e.getMessage());
            throw new CaseServiceException(e.getMessage());
        }
    }

    @Override
    public boolean add(Entity entity) throws CaseServiceException {

        boolean result = false;
        if (entity instanceof Case) {
            Case newCase = (Case) entity;
            int doctorID = newCase.getDoctorId();
            int cardID = newCase.getMedicalCardId();
            try {
                MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                cardService.setConnection(getConnection());
                MedicalCard card = (MedicalCard) cardService.getById(cardID);

                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.setConnection(getConnection());
                User user = (User) userService.getById(doctorID);

                if (user != null && card != null) {
                    result = super.add(entity);
                }
            } catch (ServiceException e){
                logger.error(e.getMessage());
                throw new CaseServiceException(e.getMessage());
            }
        }
        return result;
    }

    public boolean deleteCase(Case deletedCase){
        return caseDAO.delete(deletedCase);
    }
}
