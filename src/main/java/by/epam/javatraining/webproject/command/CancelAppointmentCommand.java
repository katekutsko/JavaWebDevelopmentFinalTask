package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.service.AppointmentService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class CancelAppointmentCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_VIEW_APPOINTMENTS;

        String appointmentId = request.getParameter(Parameters.APPOINTMENT_ID);
        if (appointmentId != null) {
            AppointmentService service = (AppointmentService) ServiceFactory.getService(ServiceType.APPOINTMENT_SERVICE);
            service.takeConnection();
            try {
                Appointment appointment = service.getById(Integer.parseInt(appointmentId));
                if (!service.delete(appointment)) {
                    request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    logger.error("could not delete appointment, id was " + appointmentId);
                    page = Pages.REDIRECT_ERROR_PAGE;
                }
            } catch (ServiceException e) {
                page = Pages.REDIRECT_ERROR_PAGE;
                request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                logger.error(e.getMessage());
            }
            service.releaseConnection();
        }
        return page;
    }
}
