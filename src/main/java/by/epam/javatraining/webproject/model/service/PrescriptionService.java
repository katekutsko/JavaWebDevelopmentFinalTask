package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.PrescriptionDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.exception.PrescriptionDAOException;

import java.util.List;

public class PrescriptionService extends Service {

    private PrescriptionDAO prescriptionDAO;

    {
        prescriptionDAO = (PrescriptionDAO) DAOFactory.getDAO(DAOType.PRESCRIPTION_DAO);
        dao = prescriptionDAO;
    }

    public List<Prescription> getAllByCaseId(int id) {
        List<Prescription> prescriptionList = null;
        try {
            prescriptionList = prescriptionDAO.getAllByCaseId(id);
        } catch (PrescriptionDAOException e) {
            logger.error(e.getMessage());
        }
        return prescriptionList;
    }
}
