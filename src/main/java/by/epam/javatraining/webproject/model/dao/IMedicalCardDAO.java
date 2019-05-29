package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.dao.exception.MedicalCardDAOException;

public interface IMedicalCardDAO extends IDAO {

    MedicalCard getByPatientId(int userId) throws MedicalCardDAOException;
}
