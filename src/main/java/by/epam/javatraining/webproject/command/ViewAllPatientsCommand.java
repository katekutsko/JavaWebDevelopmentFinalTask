package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ViewAllPatientsCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
        userService.takeConnection();

        MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
        cardService.setConnection(userService.getConnection());

        CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
        try {
            List<User> userList = userService.getAllOfType(UserRole.PATIENT);

            List<MedicalCard> cardList = cardService.getAll();

            List<Case> lastCasesList = new ArrayList<>();

            if (userList != null && cardList != null) {
                caseService.setConnection(userService.getConnection());

                for (MedicalCard card : cardList) {
                    try {
                        lastCasesList.add(caseService.getLastCaseByPatientId(card.getId()));
                    } catch (CaseServiceException e) {
                        logger.error(e.getMessage());
                    }

                    logger.info("patients were found and set as attributes");
                    logger.info("amount of patients: " + userList.size());
                }
                request.setAttribute(Parameters.PATIENTS, userList);
                request.setAttribute(Parameters.CARDS, cardList);
                request.setAttribute(Parameters.CASES, lastCasesList);
                request.setAttribute(Parameters.AMOUNT, userList.size());

                page = Pages.FORWARD_VIEW_ALL_PATIENTS;
            } else {
                logger.warn("patients were not found");
            }
        } catch (MedicalCardServiceException | UserServiceException e) {
            logger.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            userService.releaseConnection();
        }
        return page;
    }

}
