package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.PrescriptionServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewPrescriptionsCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;
        MedicalCard card = (MedicalCard) request.getSession().getAttribute(Parameters.CARD);

        if (card != null) {
            logger.debug("card id is " + card.getId());
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.takeConnection();

            try {
                Case lastCase = caseService.getLastCaseByPatientId(card.getId());
                if (lastCase != null && lastCase.getActive() == 1) {
                    PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
                    prescriptionService.setConnection(caseService.getConnection());
                    List<Prescription> prescriptionList = prescriptionService.getAllByCaseId(lastCase.getId());
                    request.setAttribute(Parameters.PRESCRIPTIONS, prescriptionList);
                } else {
                    request.setAttribute(Parameters.MESSAGE, Messages.NO_RESULTS);
                }
                page = Pages.VIEW_PRESCRIPTIONS;
            } catch (PrescriptionServiceException | CaseServiceException e) {
                logger.error(e.getMessage());
            }
            caseService.releaseConnection();
        }
        return page;
    }
}
