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

public class PatientFilter implements Filter {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    private String contextPath;
    private List<String> patientCommands;
    private List<String> patientRelatedCommands;
    private String DELIMETER = " ";
    private String PATIENT_COMMANDS = "patientCommands";
    private String PATIENT_RELATED_COMMANDS = "patientRelatedCommands";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        String[] commands = filterConfig.getInitParameter(PATIENT_COMMANDS).split(DELIMETER);
        patientCommands = Arrays.asList(commands);
        commands = filterConfig.getInitParameter(PATIENT_COMMANDS).split(DELIMETER);
        patientRelatedCommands = Arrays.asList(commands);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("patient filter");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String command = request.getParameter(Parameters.COMMAND);
        User user = (User) request.getSession().getAttribute(Parameters.USER);
        UserRole role = null;

        if (user != null) {
           role = user.getRole();
        }
        if (role != null && (role != UserRole.PATIENT && patientCommands.contains(command))
                || (role == UserRole.ADMINISTRATOR && patientRelatedCommands.contains(command))) {

            logger.debug("non-patient user tried to perform " + command);
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
