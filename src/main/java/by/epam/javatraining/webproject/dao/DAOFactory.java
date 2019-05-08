package by.epam.javatraining.webproject.dao;

public class DAOFactory {

    public static IDAO getDAO(DAOType type){
        return type.getDAO();
    }
}
