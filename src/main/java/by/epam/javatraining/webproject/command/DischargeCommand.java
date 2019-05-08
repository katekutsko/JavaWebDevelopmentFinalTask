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
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DischargeCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
        //DOMConfigurator.configure("D:\\Workspace\\WebProject\\resource\\log4j.xml");
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = null;
        if (type == ActionType.POST){

            String card = request.getParameter("card_id");
            String doctor = request.getParameter("doctor_id");
            String diagnosisString = request.getParameter("final_diagnosis");
            String date = request.getParameter("discharge_date");
            String lastCaseId = request.getParameter("last_case");

            logger.info("dischargement data: " + card + doctor + diagnosisString + date + lastCaseId);

            if (diagnosisString != null && date != null && lastCaseId != null && !lastCaseId.equals("")) {
                ConnectionPool pool = ConnectionPool.getInstance();

                Diagnosis diagnosis = Diagnosis.valueOf(diagnosisString);
                CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
                caseDAO.getConnection(pool);

                try {
                    Case lastCase = (Case) caseDAO.getById(Integer.parseInt(lastCaseId));
                    lastCase.setFinalDiagnosis(diagnosis);
                    lastCase.setDischargeDate(date);
                    logger.debug("updated case in code");

                    if (caseDAO.closeCase(lastCase)){
                        logger.info("successful dischargement");
                        request.removeAttribute("patient");
                        request.removeAttribute("card");
                        request.removeAttribute("last_case");
                        request.removeAttribute("discharge_date");
                    } else {
                        logger.info("not discharged");
                    }
                } catch (CaseDAOException e) {
                    logger.error(e.getMessage());
                }
                caseDAO.releaseConnection(pool);
            }
            page = Pages.REDIRECT_VIEW_ALL_PATIENTS;
        } else {
            request.setAttribute("diagnoses", Diagnosis.values());
            request.setAttribute("discharge_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
            page = Pages.FORWARD_DISCHARGE;
        }
        return page;
    }
}
