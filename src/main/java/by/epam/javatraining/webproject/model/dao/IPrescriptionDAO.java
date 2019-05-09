package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.exception.PrescriptionDAOException;

import java.util.List;

public interface IPrescriptionDAO extends IDAO {
    List<Prescription> getAllByCaseId(int currentCaseId) throws PrescriptionDAOException;

    List<Prescription> getByPatientId(int userId) throws PrescriptionDAOException;
}
