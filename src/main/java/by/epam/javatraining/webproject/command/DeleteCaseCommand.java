package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import by.epam.javatraining.webproject.util.configuration.ConfigurationData;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.http.HttpServletRequest;

public class DeleteCaseCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
        DOMConfigurator.configure(ConfigurationData.getString(ConfigurationData.LOG4J_XML));
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = Pages.REDIRECT_ERROR_PAGE;

        String caseId = request.getParameter(Parameters.CASE_ID);
        int caseID = 0;
        if (caseId != null) {
            caseID = Integer.parseInt(caseId);
        }
        if (caseID != 0) {
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
            caseService.takeConnection();
            Case currentCase = null;
            try {
                currentCase = (Case) caseService.getById(caseID);
                if (caseService.delete(currentCase)) {
                    int cardID = currentCase.getMedicalCardId();
                    MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                    cardService.takeConnection();
                    MedicalCard card = cardService.getByPatientId(cardID);

                    int userID = 0;
                    if (card != null) {
                        userID = card.getUserID();
                    }

                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.setConnection(cardService.getConnection());
                    String name = userService.getNameByID(userID);
                    userService.releaseConnection();

                    page = Pages.REDIRECT_VIEW_PATIENT + "&" + Parameters.CARD_ID + "=" + cardID
                            + "&" + Parameters.NAME + "=" + name;
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            }
        }
        return page;
    }
}

