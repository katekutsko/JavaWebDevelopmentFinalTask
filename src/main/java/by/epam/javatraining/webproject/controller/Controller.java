package by.epam.javatraining.webproject.controller;

import by.epam.javatraining.webproject.command.Command;
import by.epam.javatraining.webproject.command.CommandManager;
import by.epam.javatraining.webproject.command.CommandType;
import by.epam.javatraining.webproject.command.RegisterCommand;
import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    private static final Logger logger;

    static {
        logger = Logger.getRootLogger();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        process(request, response, ActionType.POST);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response, ActionType.GET);
    }

    private void process(HttpServletRequest request, HttpServletResponse response, ActionType type) {

        String commandName = request.getParameter(Parameters.COMMAND);
        Command command = CommandManager.getCommand(commandName);
        logger.debug(commandName + " extracted");
        String page = command.execute(request, type);

        try {
            if (type == ActionType.GET) {
                logger.debug("forwarded to " + page);
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                logger.debug("redirected to " + page);
                response.sendRedirect(request.getContextPath() + "/" + page);
            }
        } catch (ServletException | IOException e) {
            logger.error("exception in servlet: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().closeAll();
    }
}
