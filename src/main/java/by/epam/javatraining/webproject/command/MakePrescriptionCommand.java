package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.PrescriptionType;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
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

            String patient = request.getParameter("card_id");
            logger.debug("card id was " + patient);

            String dateAsString = request.getParameter("date");
            String details = request.getParameter("details");
            String prescriptionType = request.getParameter("type");
            String doctor = request.getParameter("doctor_id");
            String caseId = request.getParameter("last_case_id");

            logger.debug("data for validation: " + prescriptionType + doctor + patient + details + caseId);

            if (prescriptionType != null && !doctor.equals("0") && !patient.equals("0") && !caseId.equals("0")) {

                PrescriptionType pType = PrescriptionType.valueOf(prescriptionType);
                int patientID = Integer.parseInt(patient);
                int doctorID = Integer.parseInt(doctor);
                int caseID = Integer.parseInt(caseId);

                Prescription prescription = new Prescription(patientID, doctorID, details, dateAsString, pType, caseID);
                PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
                prescriptionService.getConnection();

                if(prescriptionService.add(prescription)) {
                    page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + patientID;
                    logger.info("insertion was successful");
                    request.removeAttribute("patient");
                    request.removeAttribute("card");
                    request.removeAttribute("last_case");
                } else {
                    logger.error("could not insert a prescription");
                }
                prescriptionService.releaseConnection();
            }
        } else {
            long now = new java.util.Date().getTime();
            request.setAttribute("types", PrescriptionType.values());
            request.setAttribute("date", new Date(now));
            page = Pages.FORWARD_PRESCRIPTION_MAKING;
        }
        return page;
    }
}
