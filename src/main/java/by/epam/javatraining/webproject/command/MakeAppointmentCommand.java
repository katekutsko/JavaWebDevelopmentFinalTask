package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.AppointmentService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.AppointmentServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MakeAppointmentCommand implements Command{

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_VIEW_APPOINTMENTS;
        HttpSession session = request.getSession();

        if (type == ActionType.POST) {
            String doctorId = request.getParameter(Parameters.DOCTOR_ID);
            String date = request.getParameter(Parameters.DATE);
            MedicalCard card = (MedicalCard) request.getSession().getAttribute(Parameters.CARD);

            if (card != null) {
                int cardId = card.getId();
                AppointmentService service = (AppointmentService) ServiceFactory.getService(ServiceType.APPOINTMENT_SERVICE);
                service.takeConnection();
                try {
                    if (!service.add(new Appointment(Integer.parseInt(doctorId), cardId, true, date))) {
                        page = Pages.REDIRECT_ERROR_PAGE;
                        logger.error("appointment wasn't made");
                        request.getSession().setAttribute(Parameters.ERROR, Messages.APPOINTMENT_NOT_MADE);
                    }
                } catch (AppointmentServiceException e) {
                    logger.error(e);
                    page = Pages.REDIRECT_ERROR_PAGE;
                    logger.error("appointment wasn't made: " + e.getMessage());
                    request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                } finally {
                    service.releaseConnection();
                }
            }
        }
        return page;
    }
}
