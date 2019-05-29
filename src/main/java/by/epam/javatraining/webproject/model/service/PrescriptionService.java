package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.implementation.PrescriptionDAO;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.dao.exception.PrescriptionDAOException;
import by.epam.javatraining.webproject.model.service.exception.PrescriptionServiceException;

import java.util.List;

public class PrescriptionService extends Service {

    private PrescriptionDAO prescriptionDAO;

    {
        prescriptionDAO = (PrescriptionDAO) DAOFactory.getDAO(DAOType.PRESCRIPTION_DAO);
        dao = prescriptionDAO;
    }

    public List<Prescription> getAllByCaseId(int id) throws PrescriptionServiceException {
       try {
            return prescriptionDAO.getAllByCaseId(id);
        } catch (PrescriptionDAOException e) {
            logger.error(e.getMessage());
            throw new PrescriptionServiceException(e.getMessage());
        }
    }

    public List<Prescription> getByPatientId(int id) throws PrescriptionServiceException {
        try {
            return prescriptionDAO.getByPatientId(id);
        } catch (PrescriptionDAOException e) {
            logger.error(e.getMessage());
            throw new PrescriptionServiceException(e.getMessage());
        }
    }
}
