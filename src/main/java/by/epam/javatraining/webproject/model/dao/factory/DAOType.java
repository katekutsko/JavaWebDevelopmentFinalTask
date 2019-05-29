package by.epam.javatraining.webproject.model.dao.factory;

import by.epam.javatraining.webproject.model.dao.*;
import by.epam.javatraining.webproject.model.dao.implementation.*;

public enum DAOType {

    USER_DAO(new UserDAO()),
    MEDICAL_CARD_DAO(new MedicalCardDAO()),
    CASE_DAO(new CaseDAO()),
    PRESCRIPTION_DAO(new PrescriptionDAO()),
    APPOINTMENT_DAO(new AppointmentDAO());

    private IDAO dao;

    private DAOType(IDAO dao){

        this.dao = dao;
    }

    public IDAO getDAO(){
        return dao;
    }
}
