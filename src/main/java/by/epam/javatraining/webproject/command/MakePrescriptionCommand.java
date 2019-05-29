package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.prescriptiontype.PrescriptionType;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

public class MakePrescriptionCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.POST) {

            String patient = request.getParameter(Parameters.CARD_ID);
            String dateAsString = request.getParameter(Parameters.DATE);
            String details = request.getParameter(Parameters.DETAILS);
            String prescriptionType = request.getParameter(Parameters.TYPE);
            String doctor = request.getParameter(Parameters.DOCTOR_ID);
            String caseId = request.getParameter(Parameters.LAST_CASE_ID);

            logger.debug("data for validation: " + prescriptionType + doctor + patient + details + caseId);

            if (prescriptionType != null) {
                PrescriptionType pType = PrescriptionType.valueOf(prescriptionType);
                int patientID = Integer.parseInt(patient);
                int doctorID = Integer.parseInt(doctor);
                int caseID = Integer.parseInt(caseId);

                Prescription prescription = new Prescription(patientID, doctorID, details, dateAsString, pType, caseID);
                PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
                prescriptionService.takeConnection();

                try {
                    if (prescriptionService.add(prescription)) {
                        page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + patientID;
                        logger.info("insertion was successful");
                    }
                } catch (ServiceException e) {
                    logger.error("could not insert a prescription " + e.getMessage());
                }
                prescriptionService.releaseConnection();
            }
        } else {
            long now = new java.util.Date().getTime();
            request.setAttribute(Parameters.TYPES, PrescriptionType.values());
            request.setAttribute(Parameters.DATE, new Date(now));
            page = Pages.FORWARD_PRESCRIPTION_MAKING;
        }
        return page;
    }
}
