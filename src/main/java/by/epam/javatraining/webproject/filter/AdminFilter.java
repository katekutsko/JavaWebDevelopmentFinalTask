package by.epam.javatraining.webproject.filter;

import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminFilter implements Filter {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    private String contextPath;
    private List<String> adminCommands;
    private String DELIMETER = " ";
    private String ADMIN_COMMANDS = "adminCommands";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        String[] commands = filterConfig.getInitParameter(ADMIN_COMMANDS).split(DELIMETER);
        adminCommands = Arrays.asList(commands);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("admin filter");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String command = request.getParameter(Parameters.COMMAND);
        User user = (User) request.getSession().getAttribute(Parameters.USER);

        if (user != null && user.getRole() != UserRole.ADMINISTRATOR && adminCommands.contains(command)) {
            logger.debug("non-admin user tried to perform " + command);
            response.sendRedirect(contextPath + "/" + Pages.REDIRECT_ERROR_PAGE + "&" + Parameters.ERROR
            + "=no_access");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
