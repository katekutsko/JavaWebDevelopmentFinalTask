package by.epam.javatraining.webproject.dao;

import by.epam.javatraining.webproject.entity.Entity;
import by.epam.javatraining.webproject.exception.DAOException;
import by.epam.javatraining.webproject.exception.PrescriptionDAOException;
import by.epam.javatraining.webproject.exception.UserDAOException;

import java.util.List;

public interface IDAO <T extends Entity> {

        List<T> getAll() throws DAOException;
        T getById(int id) throws DAOException;
        boolean insert(T entity) throws DAOException;
        boolean delete(T entity)  throws DAOException;
        boolean update(T entity) throws DAOException;
}
