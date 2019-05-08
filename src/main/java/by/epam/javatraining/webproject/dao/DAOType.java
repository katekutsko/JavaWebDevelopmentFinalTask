package by.epam.javatraining.webproject.dao;

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
