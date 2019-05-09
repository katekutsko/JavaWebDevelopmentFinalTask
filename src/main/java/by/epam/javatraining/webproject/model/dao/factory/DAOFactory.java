package by.epam.javatraining.webproject.model.dao.factory;

import by.epam.javatraining.webproject.model.dao.IDAO;

public class DAOFactory {

    public static IDAO getDAO(DAOType type){
        return type.getDAO();
    }
}
