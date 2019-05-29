package by.epam.javatraining.webproject.filter;

import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UnregisteredUserFilter implements Filter {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    private String contextPath;
    private List<String> unauthorizedCommands;
    private String DELIMETER = " ";
    private String UNAUTHORIZED_COMMANDS = "unauthorizedCommands";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        String[] commands = filterConfig.getInitParameter(UNAUTHORIZED_COMMANDS).split(DELIMETER);
        unauthorizedCommands = Arrays.asList(commands);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("unregistered user filter");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String command = request.getParameter(Parameters.COMMAND);
        Object user = request.getSession().getAttribute(Parameters.USER);

        if (user == null && !unauthorizedCommands.contains(command)) {
            logger.debug("unregistered user tried to perform " + command);
            response.sendRedirect(contextPath + "/" + Pages.REDIRECT_LOGIN);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
