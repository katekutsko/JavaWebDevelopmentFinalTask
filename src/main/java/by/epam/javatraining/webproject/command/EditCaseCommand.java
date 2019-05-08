package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.CaseDAO;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.Case;
import by.epam.javatraining.webproject.entity.Diagnosis;
import by.epam.javatraining.webproject.exception.CaseDAOException;
import by.epam.javatraining.webproject.util.Pages;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCaseCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            String caseId = request.getParameter("case_id");
            String patientName = request.getParameter("name");
            logger.info("preparing edit case page");
            logger.info("parameters (case id and name): " + caseId + " " + patientName);

            if (caseId != null && !caseId.equals("") && patientName != null && !patientName.equals("")) {
                int caseID = Integer.parseInt(caseId);
                ConnectionPool pool = ConnectionPool.getInstance();
                CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
                caseDAO.getConnection(pool);

                if (caseID != 0) {

                    try {
                        Case currentCase = (Case) caseDAO.getById(caseID);

                        String admissionDate = currentCase.getAdmissionDate();
                        Diagnosis diagnosis = currentCase.getFinalDiagnosis();
                        String dischargeDate = currentCase.getDischargeDate();
                        int doctorId = currentCase.getDoctorId();
                        int cardId = currentCase.getMedicalCardId();
                        String complaints = currentCase.getComplaints();
                        logger.info("GET: " + cardId + admissionDate + dischargeDate + complaints + diagnosis);

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

                    } catch (CaseDAOException e) {
                        logger.info("could not prepare edit case page: " + e.getMessage());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } finally {
                        caseDAO.getConnection(pool);
                    }

                }
            }
        } else {
            String cardIdAsString = request.getParameter("card_id");
            String admissionDateAsString = request.getParameter("admission_date");
            String dischargeDateAsString = request.getParameter("discharge_date");
            String caseIdAsString = request.getParameter("case_id");
            String complaints = request.getParameter("complaints");
            String diagnosisAsString = request.getParameter("final_diagnosis");
            logger.info(cardIdAsString + admissionDateAsString + dischargeDateAsString + caseIdAsString + complaints + diagnosisAsString);

            if (caseIdAsString != null && !caseIdAsString.isEmpty()){

                CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
                ConnectionPool pool = ConnectionPool.getInstance();
                caseDAO.getConnection(pool);

                logger.info("case id was " + caseIdAsString);
                int caseId = Integer.parseInt(caseIdAsString);
                try {
                    Case foundCase = (Case) caseDAO.getById(caseId);
                    foundCase.setFinalDiagnosis(Diagnosis.valueOf(diagnosisAsString.toUpperCase()));
                    foundCase.setAdmissionDate(admissionDateAsString);
                    foundCase.setDischargeDate(dischargeDateAsString);
                    foundCase.setComplaints(complaints);

                    if (caseDAO.update(foundCase)) {
                        int cardID = Integer.parseInt(cardIdAsString);
                        page = Pages.REDIRECT_VIEW_PATIENT + "&card_id=" + cardID;
                    }
                } catch (CaseDAOException e) {
                    logger.info("case was not extracted: " + e.getMessage());
                } finally {
                    caseDAO.releaseConnection(pool);
                }
            }
        }
        return page;
    }

}