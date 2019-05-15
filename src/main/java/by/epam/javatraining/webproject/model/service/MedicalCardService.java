package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.MedicalCardDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.exception.MedicalCardDAOException;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;

import java.util.List;

public class MedicalCardService extends Service {

    private MedicalCardDAO cardDAO;

    {
        cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
        dao = cardDAO;
    }

    public MedicalCard getByPatientId(int patientId) throws MedicalCardServiceException {

        try {
            return cardDAO.getByPatientId(patientId);
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
            throw new MedicalCardServiceException(e.getMessage());
        }
    }

    public boolean addCard(MedicalCard card) throws MedicalCardServiceException {
       boolean result = false;
        try {
            result =  cardDAO.insert(card);;
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
            throw new MedicalCardServiceException(e.getMessage());
        }
        return result;
    }

    public int getIdByUserId(int userId) throws MedicalCardServiceException {
        int id = 0;
        try {
            MedicalCard card = cardDAO.getByPatientId(userId);
            if (card != null){
                id = card.getId();
            }
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
            throw new MedicalCardServiceException(e.getMessage());
        }
        return id;
    }

    public List<MedicalCard> getAll() throws MedicalCardServiceException {
        List<MedicalCard> cards = null;
        try {
            cards = cardDAO.getAll();
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
            throw new MedicalCardServiceException(e.getMessage());
        }
        return cards;
    }
}
