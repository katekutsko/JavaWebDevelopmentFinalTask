package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
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
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                if (user.getRole() == UserRole.PATIENT) {
                    MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                    cardService.getConnection();
                    MedicalCard card = cardService.getByPatientId(user.getId());
                    request.getSession().setAttribute("medical_card", card);
                    cardService.releaseConnection();
                }
                page = Pages.FORWARD_VIEW_PROFILE;
            }
        }
        return page;
    }
}
