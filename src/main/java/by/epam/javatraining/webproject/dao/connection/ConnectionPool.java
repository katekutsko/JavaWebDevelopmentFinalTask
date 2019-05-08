package by.epam.javatraining.webproject.dao.connection;

import by.epam.javatraining.webproject.util.configuration.ConfigurationData;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final int CONNECTIONS_AMOUNT = 10;

    private static ConnectionPool instance;
    private static BlockingQueue<Connection> connections;
    private static Logger logger;

     static {
        logger = Logger.getRootLogger();

        instance = new ConnectionPool();

        try {
            Class.forName(ConfigurationData.getString(ConfigurationData.DRIVER_PATH));
        } catch (ClassNotFoundException e) {
            logger.error("Failed to find class. " + e.getMessage(), e);
        }

        connections = new ArrayBlockingQueue<Connection>(CONNECTIONS_AMOUNT);

        for (int i = 0; i < CONNECTIONS_AMOUNT; i++) {
            try {
                connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH),
                        ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN), ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
            } catch (SQLException e) {
                logger.error("Failed to get connection. " + e.getMessage(), e);
            }
        }
    }

    public static ConnectionPool getInstance(){
         return instance;
    }

    public Connection getConnection(){
         Connection newConnection = null;
        try {
            newConnection = connections.take();
        } catch (InterruptedException e) {
            logger.error("user thread was interrupted");
        }

        if (newConnection == null) {
             try {
                 logger.info("connection was null, creating new connection");
                 connections.add(DriverManager.getConnection(ConfigurationData.getString(ConfigurationData.DATABASE_CONNECTION_PATH),
                         ConfigurationData.getString(ConfigurationData.DATABASE_LOGIN), ConfigurationData.getString(ConfigurationData.DATABASE_PASSWORD)));
                 newConnection = connections.poll();
             } catch (SQLException e) {
                 logger.error("Error while creating new сonnection");
             }
        }
        logger.info("obtained connection");
         return newConnection;
    }

    public void releaseConnection(Connection connection) {

         if (connection != null){
             logger.info("returned connection");
             connections.add(connection);
         }
    }
}
