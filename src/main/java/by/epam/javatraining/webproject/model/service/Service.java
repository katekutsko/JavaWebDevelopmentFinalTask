package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.exception.CommitException;
import by.epam.javatraining.webproject.model.exception.DAOException;
import org.apache.log4j.Logger;

import java.util.List;

public class Service {

    protected Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    private static ConnectionPool pool;
    protected AbstractDAO dao;

    static {
        pool = ConnectionPool.getInstance();
    }

    public void getConnection() {
        dao.getConnection(pool);
    }

    public void releaseConnection() {
        dao.releaseConnection(pool);
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

    public Entity getById(int caseId) {

        Entity entity = null;
        try {
            entity = dao.getById(caseId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return entity;
    }

    public boolean edit(Entity entity) {
        boolean result = false;
        try {
            dao.update(entity);
            result= true;
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean add(Entity entity) {
        boolean result = false;
        try {
            dao.insert(entity);
            result = true;
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean update(Entity entity) {
        boolean result = false;
        try {
            dao.update(entity);
            result = true;
        } catch (DAOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

}

