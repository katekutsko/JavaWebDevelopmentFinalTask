package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.dao.exception.AppointmentDAOException;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.dao.exception.DAOException;

import java.util.List;

public interface IDAO<T extends Entity> {

    List<T> getAll() throws DAOException;

    T getById(int id) throws DAOException;

    boolean insert(T entity) throws DAOException, AppointmentDAOException;

    boolean delete(T entity) throws DAOException;

    boolean update(T entity) throws DAOException;
}
