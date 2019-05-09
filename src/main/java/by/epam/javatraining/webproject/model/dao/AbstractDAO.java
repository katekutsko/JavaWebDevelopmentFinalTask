package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.model.exception.CommitException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO implements IDAO {

    protected Connection connection;

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    public void getConnection(ConnectionPool pool) {
        connection = pool.getConnection();
    }

    public void releaseConnection(ConnectionPool pool) {
        pool.releaseConnection(connection);
        connection = null;
    }

    public void setAutoCommit(boolean autocommit) throws CommitException {
        try {
            if (connection != null) {
                connection.setAutoCommit(autocommit);
            }
        } catch (SQLException e) {
            logger.error("exception while setting autocommit:" + e.getMessage());
            throw new CommitException();
        }
    }


    public void commit() throws CommitException {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            logger.error("exception while commit:" + e.getMessage());
            throw new CommitException();
        }
    }

    public void rollback() {

        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            logger.error("exception during rollback:" + e.getMessage());
        }
    }
}
