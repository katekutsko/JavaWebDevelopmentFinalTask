package by.epam.javatraining.webproject.dao;

import by.epam.javatraining.webproject.entity.MedicalCard;

import java.util.List;

public interface IMedicalCardDAO extends IDAO {

    MedicalCard getByPatientId(int userId);
    MedicalCard getByPatientFullName(String lastName, String firstName, String patronymic);
    List<MedicalCard> getByPatientLastName(String lastName);
}
