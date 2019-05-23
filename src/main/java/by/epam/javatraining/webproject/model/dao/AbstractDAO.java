package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.model.exception.CommitException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO implements IDAO {

    private ConnectionPool pool;
    protected Connection connection;

    private Logger logger;

    {
        pool = ConnectionPool.getInstance();
        logger = Logger.getRootLogger();
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void takeConnection(){
        connection = pool.getConnection();
    }

    public Connection getConnection() {
       return connection;
    }

    public void releaseConnection() {
        pool.releaseConnection(connection);
    }

    public void setAutoCommit(boolean autocommit) throws CommitException {
        try {
            if (connection != null) {
                logger.info("auocommit set to " + autocommit);
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
