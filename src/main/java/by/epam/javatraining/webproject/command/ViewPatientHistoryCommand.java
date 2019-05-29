package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.CaseService;
import by.epam.javatraining.webproject.model.service.PrescriptionService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.CaseServiceException;
import by.epam.javatraining.webproject.model.service.exception.PrescriptionServiceException;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.util.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPatientHistoryCommand implements Command {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.GET) {
            String cardIdAsString = request.getParameter(Parameters.CARD_ID);
            String name = request.getParameter(Parameters.NAME);
            logger.debug("got " + cardIdAsString + " " + name);
            int cardId = 0;

            if ((cardIdAsString == null || cardIdAsString.isEmpty()) && (name == null || name.isEmpty())) {
                User patient = (User) request.getSession().getAttribute(Parameters.USER);
                MedicalCard card = (MedicalCard) request.getSession().getAttribute(Parameters.MEDICAL_CARD);

                if (card != null && patient != null) {
                    name = patient.getSurname() + " " + patient.getName() + " " + patient.getPatronymic();
                    cardId = card.getId();
                }
            } else if (cardIdAsString != null) {
                    cardId = Integer.parseInt(cardIdAsString);
            }

            if (name != null && !name.isEmpty() && cardId != 0) {
                CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
                caseService.takeConnection();
                List<Case> cases = null;

                try {
                    cases = caseService.getAllCasesOfCertainPatient(cardId);
                } catch (CaseServiceException e) {
                    logger.error(e.getMessage());
                }
                request.setAttribute(Parameters.CASES, cases);
                request.setAttribute(Parameters.CARD_ID, cardId);
                request.setAttribute(Parameters.PATIENT_NAME, name);

                if (cases != null && !cases.isEmpty()) {
                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.setConnection(caseService.getConnection());
                    List<User> doctors = null;

                    try {
                        doctors = userService.getAllOfType(UserRole.DOCTOR);
                        Map<Integer, String> doctorNames = new HashMap<>();

                        for (User doctor : doctors) {
                            if (doctor != null) {
                                doctorNames.put(doctor.getId(), doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic());
                            }
                        }
                        request.setAttribute(Parameters.DOCTOR_NAMES, doctorNames);

                        PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
                        prescriptionService.setConnection(caseService.getConnection());
                        Map<Integer, List<Prescription>> prescriptionsByCaseId = new HashMap<>();

                        for (Case cur : cases) {
                            int currenctCaseId = cur.getId();
                            try {
                                List<Prescription> prescriptions = prescriptionService.getAllByCaseId(currenctCaseId);
                                prescriptionsByCaseId.put(currenctCaseId, prescriptions);
                            } catch (PrescriptionServiceException e) {
                                logger.error(e.getMessage());
                            }
                        }
                        request.setAttribute("prescriptions_by_case_id", prescriptionsByCaseId);

                        logger.info("preparing profile page finished");
                        page = Pages.VIEW_HISTORY;
                    } catch (UserServiceException e) {
                        logger.error(e.getMessage());
                        request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    }
                    caseService.releaseConnection();
                } else {
                    request.setAttribute(Parameters.MESSAGE, Messages.NO_RESULTS);
                    page = Pages.VIEW_HISTORY;
                }
            }
        }
        return page;
    }
}
