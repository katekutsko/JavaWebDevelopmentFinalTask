package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.AppointmentService;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.AppointmentServiceException;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAppointmentsCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;
        HttpSession session = request.getSession();

        AppointmentService appointmentService = (AppointmentService) ServiceFactory.getService(ServiceType.APPOINTMENT_SERVICE);
        appointmentService.takeConnection();
        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
        userService.setConnection(appointmentService.getConnection());

        try {
            MedicalCard card = (MedicalCard) session.getAttribute(Parameters.CARD);
            List<Appointment> appointments = null;
            Map<Integer, String> names = new HashMap<>();

            if (card != null) {
                appointments = appointmentService.getAllOfPatient(card.getId());
                if (appointments != null && !appointments.isEmpty()) {
                    logger.debug("appointments: " + appointments);
                    List<User> doctors = userService.getAllOfType(UserRole.DOCTOR);
                    for (User doctor : doctors) {
                        if (doctor != null) {
                            names.put(doctor.getId(), doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic());
                        }
                    }
                }
            } else {
                User user = (User) session.getAttribute(Parameters.USER);

                if (user != null && user.getRole() == UserRole.DOCTOR) {
                    appointments = appointmentService.selectActive(appointmentService.getAllOfDoctor(user.getId()));

                    MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                    cardService.setConnection(appointmentService.getConnection());

                    CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
                    caseService.setConnection(appointmentService.getConnection());

                    Map<Integer, Case> lastCasesByPatientId = new HashMap<>();

                    for (Appointment appointment : appointments) {
                        int patientId = appointment.getPatientId();
                        names.put(patientId, cardService.getNameById(patientId));
                        try {
                            lastCasesByPatientId.put(patientId, caseService.getLastCaseByPatientId(patientId));
                        } catch (CaseServiceException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    request.setAttribute(Parameters.CASES, lastCasesByPatientId);
                }
            }
            logger.debug(names);
            session.setAttribute(Parameters.NAMES, names);
            session.setAttribute(Parameters.APPOINTMENTS, appointments);
            page = Pages.VIEW_APPOINTMENTS;

        } catch (AppointmentServiceException | UserServiceException | MedicalCardServiceException e) {
            logger.error(e.getMessage());
            session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            appointmentService.releaseConnection();
        }
        return page;
    }
}
