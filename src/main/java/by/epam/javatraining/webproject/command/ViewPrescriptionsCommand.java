package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
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
        MedicalCard card = (MedicalCard) request.getSession().getAttribute("card");

        if (card != null) {

            logger.debug("card id is " + card.getId());
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);

            PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);

            caseService.getConnection();
            Case lastCase = caseService.getLastCaseByPatientId(card.getId());
            caseService.releaseConnection();

            if (lastCase != null && lastCase.getActive() == 1) {
                prescriptionService.getConnection();
                List<Prescription> prescriptionList = prescriptionService.getAllByCaseId(lastCase.getId());
                prescriptionService.releaseConnection();

                if (prescriptionList != null && !prescriptionList.isEmpty()) {
                    request.setAttribute("prescriptions", prescriptionList);
                } else {
                    request.setAttribute("message", Messages.NO_RESULTS);
                }
            } else {
                request.setAttribute("message", Messages.NO_RESULTS);
            }
            page = Pages.VIEW_PRESCRIPTIONS;
        }
        return page;
    }
}
