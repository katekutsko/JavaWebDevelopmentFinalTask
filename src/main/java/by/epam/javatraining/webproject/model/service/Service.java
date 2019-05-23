package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.exception.CommitException;
import by.epam.javatraining.webproject.model.exception.DAOException;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.sql.Connection;

public class Service {

    protected Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    protected AbstractDAO dao;

    public void setConnection(Connection connection){
        dao.setConnection(connection);
    }

    public void takeConnection(){
       dao.takeConnection();
    }

    public Connection getConnection() {
        return dao.getConnection();
    }

    public void releaseConnection() {
        dao.releaseConnection();
    }


    public boolean setAutoCommit(boolean autoCommit) {

        boolean result = false;
            try {
                dao.setAutoCommit(autoCommit);
                result = true;
            } catch (CommitException e) {
                logger.error(e.getMessage());
            }
        return result;
    }

    public void rollback() {
        dao.rollback();
    }

    public boolean commit() {
        boolean result = false;

            try {
                dao.commit();
                result = true;
            } catch (CommitException e) {
                logger.error(e.getMessage());
            }
        return result;
    }

    public Entity getById(int caseId) throws ServiceException {

        Entity entity = null;
        try {
            entity = dao.getById(caseId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return entity;
    }

    public boolean edit(Entity entity) throws ServiceException {
        boolean result = false;
        try {

            result= dao.update(entity);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    public boolean add(Entity entity) throws ServiceException {
        boolean result = false;
        try {
            result = dao.insert(entity);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    public boolean update(Entity entity) throws ServiceException {
        boolean result = false;
        try {
            result = dao.update(entity);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());

        }
        return result;
    }

    public boolean delete(Entity entity) throws ServiceException {
        boolean result = false;
        try {
            result = dao.delete(entity);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }
}

