package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.Diagnosis;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DischargeCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = null;
        if (type == ActionType.POST) {

            String card = request.getParameter(Parameters.CARD_ID);
            String doctor = request.getParameter(Parameters.DOCTOR_ID);
            String diagnosisString = request.getParameter(Parameters.FINAL_DIAGNOSIS);
            String date = request.getParameter(Parameters.DISCHARGE_DATE);
            String lastCaseId = request.getParameter(Parameters.LAST_CASE);

            logger.info("dischargement data: " + card + doctor + diagnosisString + date + lastCaseId);

            if (diagnosisString != null && date != null && lastCaseId != null && !lastCaseId.equals("")) {

                Diagnosis diagnosis = Diagnosis.valueOf(diagnosisString.toUpperCase());
                CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);

                caseService.takeConnection();

                try {
                    Case lastCase = (Case) caseService.getById(Integer.parseInt(lastCaseId));
                    lastCase.setFinalDiagnosis(diagnosis);
                    lastCase.setDischargeDate(date);
                    logger.debug("updated case in code");

                    if (caseService.closeCase(lastCase)) {
                        logger.info("successful dischargement");
                        request.removeAttribute(Parameters.PATIENT);
                        request.removeAttribute(Parameters.CARD);
                        request.removeAttribute(Parameters.LAST_CASE);
                        request.removeAttribute(Parameters.DISCHARGE_DATE);
                        page = Pages.REDIRECT_VIEW_ALL_PATIENTS;
                    } else {
                        logger.info("not discharged");
                    }
                } catch (ServiceException e){
                    logger.error(e.getMessage());
                }
                caseService.releaseConnection();
            }
        } else {
            request.setAttribute(Parameters.DIAGNOSES, Diagnosis.values());
            request.setAttribute(Parameters.DISCHARGE_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
            page = Pages.FORWARD_DISCHARGE;
        }
        return page;
    }
}

