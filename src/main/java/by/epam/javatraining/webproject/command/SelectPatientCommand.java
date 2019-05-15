package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class SelectPatientCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }


    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            String cardId = request.getParameter(Parameters.CARD_ID);
            int id = Integer.parseInt(cardId);

            MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
            cardService.takeConnection();
            try {
                MedicalCard card = (MedicalCard) cardService.getById(id);

                if (card != null) {
                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.setConnection(cardService.getConnection());
                    User user = (User) userService.getById(card.getUserID());

                    CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
                    caseService.setConnection(cardService.getConnection());
                    Case lastCase = caseService.getLastCaseByPatientId(id);

                    request.setAttribute(Parameters.LAST_CASE, lastCase);
                    request.setAttribute(Parameters.PATIENT, user);
                    request.setAttribute(Parameters.CARD, card);

                    page = Pages.FORWARD_VIEW_PATIENT;
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
            }
            cardService.releaseConnection();
        }
        return page;
    }
}
