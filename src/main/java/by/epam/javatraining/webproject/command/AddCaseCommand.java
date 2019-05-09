package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
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

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            String cardId = request.getParameter("card_id");
            String patientName = request.getParameter("name");

            if (cardId != null && !cardId.equals("") && patientName != null && !patientName.equals("")) {
                int cardID = Integer.parseInt(cardId);
                String admissionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                request.setAttribute("patient_name", patientName);
                request.setAttribute("card_id", cardID);
                request.setAttribute("admission_date", admissionDate);
            }
            page = Pages.FORWARD_ADD_CASE;

        } else {
            String cardId = request.getParameter("card_id");
            String complaints = request.getParameter("complaints");
            String doctorId = request.getParameter("doctor_id");
            String admissionDate = request.getParameter("admission_date");

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

                if (caseService.add(newCase)) {
                    request.removeAttribute("patient_name");
                    request.removeAttribute("card_id");
                    request.removeAttribute("admission_date");
                } else {
                    logger.error("could not add case");
                    request.setAttribute("errorMessage", Messages.ACTION_NOT_PERFORMED);
                }
                caseService.releaseConnection();
                page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + cardId;
            }
            logger.error("case was not recorded");
            request.setAttribute("errorMessage", Messages.FIELDS_NOT_FILLED);
        }
        return page;
    }
}
