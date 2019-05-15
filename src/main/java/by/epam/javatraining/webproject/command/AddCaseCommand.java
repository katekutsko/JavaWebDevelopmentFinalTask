package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCaseCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_ERROR_PAGE;

        if (type == ActionType.GET) {
            String cardId = request.getParameter(Parameters.CARD_ID);
            String patientName = request.getParameter(Parameters.NAME);

            if (cardId != null && !cardId.equals("") && patientName != null && !patientName.equals("")) {
                int cardID = Integer.parseInt(cardId);
                String admissionDate = new SimpleDateFormat(Parameters.DATE_PATTERN).format(new Date());

                request.setAttribute(Parameters.PATIENT_NAME, patientName);
                request.setAttribute(Parameters.CARD_ID, cardID);
                request.setAttribute(Parameters.ADMISSION_DATE, admissionDate);
                page = Pages.FORWARD_ADD_CASE;
            } else {
                page = Pages.ERROR_PAGE;
            }

        } else {
            String cardId = request.getParameter(Parameters.CARD_ID);
            String complaints = request.getParameter(Parameters.COMPLAINTS);
            String doctorId = request.getParameter(Parameters.DOCTOR_ID);
            String admissionDate = request.getParameter(Parameters.ADMISSION_DATE);

            logger.debug("adding case with parameters: " + cardId + complaints + doctorId + admissionDate);

            if (cardId != null && !cardId.equals("") && complaints != null && !complaints.equals("") && doctorId != null &&
                    !doctorId.equals("") && admissionDate != null && !admissionDate.equals("")) {

                Case newCase = new Case();
                newCase.setDoctorId(Integer.parseInt(doctorId));
                newCase.setComplaints(complaints);
                newCase.setAdmissionDate(admissionDate);
                newCase.setMedicalCardId(Integer.parseInt(cardId));
                logger.info(newCase + " created");

                CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
                caseService.getConnection();

                try {
                    if (caseService.add(newCase)) {

                        request.removeAttribute(Parameters.PATIENT_NAME);
                        request.removeAttribute(Parameters.CARD_ID);
                        request.removeAttribute(Parameters.ADMISSION_DATE);
                        caseService.releaseConnection();
                        page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + cardId;
                    } else {
                        logger.error("could not add case");
                        request.getSession().setAttribute(Parameters.ERROR, Messages.ACTION_NOT_PERFORMED);
                    }
                } catch (CaseServiceException e) {
                    logger.error(e.getMessage());
                }
            }
            logger.error("case was not recorded");
            request.setAttribute(Parameters.ERROR, Messages.FIELDS_NOT_FILLED);
        }
        return page;
    }
}
