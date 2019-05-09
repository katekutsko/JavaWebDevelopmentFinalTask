package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.exception.MedicalCardDAOException;

import java.util.List;

public interface IMedicalCardDAO extends IDAO {

    MedicalCard getByPatientId(int userId) throws MedicalCardDAOException;
    MedicalCard getByPatientFullName(String lastName, String firstName, String patronymic);
    List<MedicalCard> getByPatientLastName(String lastName);
}
