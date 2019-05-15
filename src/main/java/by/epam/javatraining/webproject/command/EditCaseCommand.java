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

        String page = Pages.ERROR_PAGE;
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
        logger.info("preparing edit case page");
        logger.info("parameters (case id and name): " + caseId + " " + patientName);

        if (caseId != null && !caseId.equals("") && patientName != null && !patientName.equals("")) {
            int caseID = Integer.parseInt(caseId);
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.takeConnection();

            if (caseID != 0) {
                try {
                    Case currentCase = null;
                    try {
                        currentCase = (Case) caseService.getById(caseID);
                    } catch (ServiceException e) {
                        logger.error(e.getMessage());
                        request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    }
                    if (currentCase != null) {
                        String admissionDate = currentCase.getAdmissionDate();
                        Diagnosis diagnosis = currentCase.getFinalDiagnosis();
                        String dischargeDate = currentCase.getDischargeDate();
                        int cardId = currentCase.getMedicalCardId();
                        String complaints = currentCase.getComplaints();
                        logger.info("GOT: " + cardId + admissionDate + dischargeDate + complaints + diagnosis);

                        request.setAttribute(Parameters.PATIENT_NAME, patientName);
                        request.setAttribute(Parameters.CARD_ID, cardId);
                        SimpleDateFormat sdf = new SimpleDateFormat(Parameters.DATE_PATTERN);
                        request.setAttribute(Parameters.ADMISSION_DATE, sdf.format(sdf.parse(admissionDate)));
                        if (dischargeDate != null && !dischargeDate.isEmpty()) {
                            request.setAttribute(Parameters.DISCHARGE_DATE, sdf.format(sdf.parse(dischargeDate)));
                        }
                        request.setAttribute(Parameters.DIAGNOSIS, diagnosis);
                        request.setAttribute(Parameters.CASE_ID, caseID);
                        request.setAttribute(Parameters.COMPLAINTS, complaints);
                        request.setAttribute(Parameters.DIAGNOSES, Diagnosis.values());
                        page = Pages.EDIT_CASE;
                    } else {
                        request.getSession().setAttribute(Parameters.ERROR, Messages.ACTION_NOT_PERFORMED);
                    }
                } catch (ParseException e) {
                    logger.error(e.getMessage());
                    request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                } finally {
                    caseService.releaseConnection();
                }
            }
        }
        return page;
    }

    private String processData(HttpServletRequest request) {
        String page = Pages.ERROR_PAGE;

        String cardIdAsString = request.getParameter(Parameters.CASE_ID);
        String admissionDateAsString = request.getParameter(Parameters.ADMISSION_DATE);
        String dischargeDateAsString = request.getParameter(Parameters.DISCHARGE_DATE);
        String caseIdAsString = request.getParameter(Parameters.CASE_ID);
        String complaints = request.getParameter(Parameters.COMPLAINTS);
        String diagnosisAsString = request.getParameter(Parameters.DIAGNOSIS);
        logger.info(cardIdAsString + admissionDateAsString + dischargeDateAsString + caseIdAsString + complaints + diagnosisAsString);

        if (caseIdAsString != null && !caseIdAsString.isEmpty()) {

            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.getConnection();

            logger.info("case id was " + caseIdAsString);
            int caseId = Integer.parseInt(caseIdAsString);
            try {
                Case foundCase = (Case) caseService.getById(caseId);

                if (foundCase != null) {
                    foundCase.setFinalDiagnosis(Diagnosis.valueOf(diagnosisAsString.toUpperCase()));
                    foundCase.setAdmissionDate(admissionDateAsString);
                    foundCase.setDischargeDate(dischargeDateAsString);
                    foundCase.setComplaints(complaints);

                    if (caseService.edit(foundCase)) {
                        int cardID = Integer.parseInt(cardIdAsString);
                        page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + cardID;
                    }
                }
            } catch (ServiceException e){
                logger.error(e.getMessage());
            }
            caseService.releaseConnection();
        }
        return page;
    }
}
