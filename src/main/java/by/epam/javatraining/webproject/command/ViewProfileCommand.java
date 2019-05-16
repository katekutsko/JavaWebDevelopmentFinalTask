package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ViewProfileCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = null;
        if (type == ActionType.GET) {
            User user = (User) request.getSession().getAttribute(Parameters.USER);
            if (user != null) {
                MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                try {
                    if (user.getRole() == UserRole.PATIENT) {
                        cardService.takeConnection();
                        MedicalCard card = cardService.getByPatientId(user.getId());
                        cardService.releaseConnection();
                        request.getSession().setAttribute(Parameters.MEDICAL_CARD, card);
                    }
                } catch (MedicalCardServiceException e) {
                    logger.error(e.getMessage());
                }
                page = Pages.FORWARD_VIEW_PROFILE;
            }
        }
        return page;
    }
}
