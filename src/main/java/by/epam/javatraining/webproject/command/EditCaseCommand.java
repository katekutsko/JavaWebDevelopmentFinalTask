package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.Diagnosis;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditCaseCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page;
        if (type == ActionType.GET) {
            page = preparePage(request);
        } else {
            page = processData(request);
        }
        return page;
    }

    private String preparePage(HttpServletRequest request) {
        String page = Pages.ERROR_PAGE;
        String caseId = request.getParameter(Parameters.CASE_ID);
        String patientName = request.getParameter(Parameters.NAME);
        logger.info("preparing edit case page. parameters (case id and name): " + caseId + " " + patientName);

        if (caseId != null && !caseId.equals("") && patientName != null && !patientName.equals("")) {
            int caseID = Integer.parseInt(caseId);
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.takeConnection();

            if (caseID != 0) {
                Case currentCase = null;
                try {
                    currentCase = (Case) caseService.getById(caseID);

                } catch (ServiceException e) {
                    logger.error(e.getMessage());
                    request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                }
                if (currentCase != null) {
                    logger.info("GOT: " + currentCase);

                    request.setAttribute(Parameters.PATIENT_NAME, patientName);
                    request.setAttribute(Parameters.CASE, currentCase);
                    page = Pages.EDIT_CASE;

                } else {
                    request.getSession().setAttribute(Parameters.ERROR, Messages.ACTION_NOT_PERFORMED);
                }
            }
            caseService.releaseConnection();
        } else {
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        }
        return page;
    }

    private String processData(HttpServletRequest request) {
        String page = Pages.REDIRECT_ERROR_PAGE;

        String cardIdAsString = request.getParameter(Parameters.CARD_ID);
        String admissionDateAsString = request.getParameter(Parameters.ADMISSION_DATE);
        String dischargeDateAsString = request.getParameter(Parameters.DISCHARGE_DATE);
        String caseIdAsString = request.getParameter(Parameters.CASE_ID);
        String complaints = request.getParameter(Parameters.COMPLAINTS);
        String diagnosisAsString = request.getParameter(Parameters.DIAGNOSIS);
        logger.info(cardIdAsString + admissionDateAsString + dischargeDateAsString + caseIdAsString + complaints + diagnosisAsString);

        if (caseIdAsString != null && !caseIdAsString.isEmpty()) {

            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.takeConnection();

            logger.info("case id was " + caseIdAsString);
            int caseId = Integer.parseInt(caseIdAsString);
            try {
                Case foundCase = (Case) caseService.getById(caseId);

                if (foundCase != null) {
                    if (diagnosisAsString != null && dischargeDateAsString != null) {
                        foundCase.setFinalDiagnosis(Diagnosis.valueOf(diagnosisAsString.toUpperCase()));
                        foundCase.setDischargeDate(dischargeDateAsString);
                    }
                    if (admissionDateAsString != null && complaints != null) {
                        foundCase.setAdmissionDate(admissionDateAsString);
                        foundCase.setComplaints(complaints);

                        if (caseService.edit(foundCase)) {
                            int cardID = Integer.parseInt(cardIdAsString);
                            page = Pages.REDIRECT_VIEW_PATIENT + "&" + Parameters.CARD_ID + "=" + cardID;
                        }
                    }
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
            }
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            caseService.releaseConnection();
        }
        return page;
    }
}
