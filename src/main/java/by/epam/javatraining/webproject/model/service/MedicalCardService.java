package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.MedicalCardDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.exception.MedicalCardDAOException;

public class MedicalCardService extends Service {

    MedicalCardDAO cardDAO;

    {
        cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
        dao = cardDAO;
    }

    public MedicalCard getByPatientId(int patientId) {

        try {
            return cardDAO.getByPatientId(patientId);
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public boolean addCard(MedicalCard card) {
       boolean result = false;
        try {
            cardDAO.insert(card);
            result = true;
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public int getIdByUserId(int userId) {
        int id = 0;
        try {
            MedicalCard card = cardDAO.getByPatientId(userId);
            if (card != null){
                id = card.getId();
            }
        } catch (MedicalCardDAOException e) {
            logger.error(e.getMessage());
        }
        return id;
    }
}
