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

public class DoctorFilter implements Filter {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    private String contextPath;
    private List<String> doctorCommands;
    private List<String> medicalStaffCommands;
    private String DELIMETER = " ";
    private String DOCTOR_COMMANDS = "doctorCommands";
    private String MEDICAL_STAFF_COMMANDS = "medicalStaffCommands";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        String[] commands = filterConfig.getInitParameter(DOCTOR_COMMANDS).split(DELIMETER);
        doctorCommands = Arrays.asList(commands);
        commands = filterConfig.getInitParameter(MEDICAL_STAFF_COMMANDS).split(DELIMETER);
        medicalStaffCommands = Arrays.asList(commands);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("doctor filter");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String command = request.getParameter(Parameters.COMMAND);
        User user = (User) request.getSession().getAttribute(Parameters.USER);
        UserRole role = null;

        if (user != null) {
            role = user.getRole();
        }

        if ((role != UserRole.DOCTOR && doctorCommands.contains(command))
                || (role != UserRole.DOCTOR && role != UserRole.NURSE && medicalStaffCommands.contains(command))) {
            logger.debug("non-doctor user tried to perform " + command);
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
