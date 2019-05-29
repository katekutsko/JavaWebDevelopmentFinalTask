package by.epam.javatraining.webproject.model.validation;

import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.service.AppointmentService;
import by.epam.javatraining.webproject.model.service.exception.AppointmentServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentValidation {

    private static final Logger logger;

    static {
        logger = Logger.getRootLogger();
    }

    public static boolean validateAppointment(Appointment newAppointment){
        return validateDate(newAppointment) && validateRepetition(newAppointment);
    }

    private static boolean validateDate(Appointment newAppointment){
        boolean result = false;
        if (newAppointment != null) {
            String date = newAppointment.getDate();
            SimpleDateFormat spf = new SimpleDateFormat(Parameters.DATE_PATTERN);
            try {
                Date now = spf.parse(spf.format(new Date()));
                Date appointmentDate = spf.parse(date);
                logger.info("app-t date and current date: \n" + now + "\n" + appointmentDate);
                result = !now.after(appointmentDate);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    private static boolean validateRepetition(Appointment newAppointment){
        boolean result = true;

        if (newAppointment != null) {
            int patientId = newAppointment.getPatientId();
            AppointmentService service = (AppointmentService) ServiceFactory.getService(ServiceType.APPOINTMENT_SERVICE);
            service.takeConnection();
            try {
                List<Appointment> appointments = service.getAllOfPatient(patientId);
                for (Appointment appointment : appointments){
                    if (newAppointment.equals(appointment)) {
                        logger.info("app-ts were equal: \n" + appointment + "\n" + newAppointment);
                        result = false;
                    }
                }
            } catch (AppointmentServiceException e) {
                logger.error(e.getMessage());
            } finally {
                service.releaseConnection();
            }
        }
        return result;
    }
}
