package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.exception.ServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DeleteUserCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_ERROR_PAGE;

        String id = request.getParameter(Parameters.USER_ID);
        logger.debug("id of user being deleted is " + id);

        if (id != null) {
            int userId = Integer.parseInt(id);

            UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
            userService.takeConnection();
            MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
            PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
            CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);

            User user = null;
            try {
                user = (User) userService.getById(userId);
            } catch (ServiceException e) {
                logger.error("could not get user by id " + id);
            }

            if (user != null) {
                if (user.getRole() == UserRole.PATIENT) {
                    userService.setAutoCommit(false);
                    try {
                        if (userService.delete(user)) {
                            cardService.setConnection(userService.getConnection());
                            MedicalCard card = cardService.getByPatientId(userId);

                            if (card != null) {
                                prescriptionService.setConnection(userService.getConnection());
                                List<Prescription> prescriptionList = prescriptionService.getByPatientId(card.getId());

                                caseService.setConnection(userService.getConnection());
                                List<Case> caseList = caseService.getAllCasesOfCertainPatient(card.getId());

                                if (clearPatientData(prescriptionService, caseService, cardService, prescriptionList, caseList, card)) {
                                    userService.commit();
                                    page = Pages.REDIRECT_VIEW_USERS;

                                } else {
                                    userService.rollback();
                                    request.getSession().setAttribute(Parameters.ERROR, Messages.ACTION_NOT_PERFORMED);
                                    logger.error("could not delete user");
                                }
                            }
                        }
                    } catch (ServiceException e) {
                        logger.error("could not delete user: " + e.getMessage());
                        userService.rollback();
                        request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    } finally {
                        userService.setAutoCommit(true);
                        userService.releaseConnection();
                    }
                } else {
                    try {
                        userService.delete(user);
                        page = Pages.REDIRECT_VIEW_USERS;
                    } catch (ServiceException e) {
                        logger.error("could not delete user");
                    }
                    userService.releaseConnection();
                }
            }
        }
        return page;
    }

    private boolean clearPatientData(PrescriptionService prescriptionService, CaseService caseService, MedicalCardService cardService, List<Prescription> prescriptionList, List<Case> caseList, MedicalCard card) {

        boolean next = true;

        try {
            for (Prescription prescription : prescriptionList) {
                if (!prescriptionService.delete(prescription)) {
                    next = false;
                    logger.debug("error deleting prescription");
                }
            }

            for (Case currentCase : caseList) {
                if (!caseService.delete(currentCase)) {
                    next = false;
                    logger.debug("error deleting case");
                }
            }
            if (cardService.delete(card)) {
                logger.debug("deleting card");
                next = false;
            }
        } catch (ServiceException e) {
            next = false;
        }
        return next;
    }
}
