package by.epam.javatraining.webproject.model.dao.factory;

import by.epam.javatraining.webproject.model.dao.*;
import by.epam.javatraining.webproject.model.dao.implementation.CaseDAO;
import by.epam.javatraining.webproject.model.dao.implementation.MedicalCardDAO;
import by.epam.javatraining.webproject.model.dao.implementation.PrescriptionDAO;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;

public enum DAOType {

    USER_DAO(new UserDAO()), MEDICAL_CARD_DAO(new MedicalCardDAO()), CASE_DAO(new CaseDAO()), PRESCRIPTION_DAO(new PrescriptionDAO());

    private IDAO dao;

    private DAOType(IDAO dao){

        this.dao = dao;
    }

    public IDAO getDAO(){
        return dao;
    }
}
