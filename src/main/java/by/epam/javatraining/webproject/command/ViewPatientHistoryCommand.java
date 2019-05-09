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
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
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
                String cardIdAsString = request.getParameter("card_id");
                String name = request.getParameter("name");
                logger.debug("got " + cardIdAsString + " " + name);

                int cardId = 0;

                if (cardIdAsString == null || name == null) {

                        User patient = (User) request.getSession().getAttribute("user");
                        MedicalCard card = (MedicalCard) request.getSession().getAttribute("medical_card");

                        logger.info("entered as a patient, so session attributes were extracted: " + patient + " " + card);

                        if (card != null && patient != null) {
                            name = patient.getSurname() + " " + patient.getName() + " " + patient.getPatronymic();
                            cardId = card.getId();
                        }

                } else {
                    cardId = Integer.parseInt(cardIdAsString);
                }

                if (name != null && !name.isEmpty() && cardId != 0) {
                    CaseService caseService = (CaseService) ServiceFactory.getService(ServiceType.CASE_SERVICE);
                    caseService.getConnection();
                    List<Case> cases = caseService.getAllCasesOfCertainPatient(cardId);
                    caseService.releaseConnection();

                    request.setAttribute("cases", cases);
                    request.setAttribute("patient_name", name);

                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.getConnection();
                    List<User> doctors = userService.getAllOfType(UserRole.DOCTOR);
                    userService.releaseConnection();

                    Map<Integer, String> doctorNames = new HashMap<>();

                    for (User doctor : doctors) {
                        if (doctor != null) {
                            doctorNames.put(doctor.getId(), doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic());
                        }
                    }
                    request.setAttribute("doctor_names", doctorNames);

                    PrescriptionService prescriptionService = (PrescriptionService) ServiceFactory.getService(ServiceType.PRESCRIPTION_SERVICE);
                    prescriptionService.getConnection();
                    Map<Integer, List<Prescription>> prescriptionsByCaseId = new HashMap<>();

                    for (Case cur : cases) {
                        int currenctCaseId = cur.getId();
                        List<Prescription> prescriptions = prescriptionService.getAllByCaseId(currenctCaseId);
                        prescriptionsByCaseId.put(currenctCaseId, prescriptions);
                    }

                    prescriptionService.releaseConnection();

                    request.setAttribute("prescriptions_by_case_id", prescriptionsByCaseId);
                    request.removeAttribute("patient");
                    request.removeAttribute("card");
                    request.removeAttribute("last_case");

                    logger.info("preparing profile page finished");
                    page = Pages.VIEW_HISTORY;
                }
        }
        return page;
    }
}
