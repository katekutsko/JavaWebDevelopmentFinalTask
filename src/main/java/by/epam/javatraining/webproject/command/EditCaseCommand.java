package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.Diagnosis;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
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
        String caseId = request.getParameter("case_id");
        String patientName = request.getParameter("name");
        logger.info("preparing edit case page");
        logger.info("parameters (case id and name): " + caseId + " " + patientName);

        if (caseId != null && !caseId.equals("") && patientName != null && !patientName.equals("")) {
            int caseID = Integer.parseInt(caseId);
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.getConnection();

            if (caseID != 0) {
                try {
                    Case currentCase = (Case) caseService.getById(caseID);
                    if (currentCase != null) {
                        String admissionDate = currentCase.getAdmissionDate();
                        Diagnosis diagnosis = currentCase.getFinalDiagnosis();
                        String dischargeDate = currentCase.getDischargeDate();
                        int doctorId = currentCase.getDoctorId();
                        int cardId = currentCase.getMedicalCardId();
                        String complaints = currentCase.getComplaints();
                        logger.info("GOT: " + cardId + admissionDate + dischargeDate + complaints + diagnosis);

                        request.setAttribute("patient_name", patientName);
                        request.setAttribute("card_id", cardId);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        request.setAttribute("admission_date", sdf.format(sdf.parse(admissionDate)));
                        if (dischargeDate != null && !dischargeDate.isEmpty()) {
                            request.setAttribute("dischargement_date", sdf.format(sdf.parse(dischargeDate)));
                        }
                        request.setAttribute("diagnosis", diagnosis);
                        request.setAttribute("case_id", caseID);
                        request.setAttribute("complaints", complaints);
                        request.setAttribute("diagnoses", Diagnosis.values());
                        page = Pages.EDIT_CASE;
                    }
                } catch (ParseException e) {
                    logger.error(e.getMessage());
                } finally {
                    caseService.releaseConnection();
                }
            }
        }
        return page;
    }

    private String processData(HttpServletRequest request) {
        String page = Pages.ERROR_PAGE;

        String cardIdAsString = request.getParameter("card_id");
        String admissionDateAsString = request.getParameter("admission_date");
        String dischargeDateAsString = request.getParameter("discharge_date");
        String caseIdAsString = request.getParameter("case_id");
        String complaints = request.getParameter("complaints");
        String diagnosisAsString = request.getParameter("final_diagnosis");
        logger.info(cardIdAsString + admissionDateAsString + dischargeDateAsString + caseIdAsString + complaints + diagnosisAsString);

        if (caseIdAsString != null && !caseIdAsString.isEmpty()) {

            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.getConnection();

            logger.info("case id was " + caseIdAsString);
            int caseId = Integer.parseInt(caseIdAsString);
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
            caseService.releaseConnection();
        }
        return page;
    }
}
