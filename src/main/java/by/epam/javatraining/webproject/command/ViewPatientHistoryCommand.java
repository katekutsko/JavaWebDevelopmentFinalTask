package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.*;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.Case;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.Prescription;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.*;
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
            try {
                ConnectionPool pool = ConnectionPool.getInstance();
                CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);
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

                    caseDAO.getConnection(pool);
                    List<Case> cases = caseDAO.getAllCasesOfCertainPatient(cardId);
                    caseDAO.releaseConnection(pool);
                    request.setAttribute("cases", cases);
                    request.setAttribute("patient_name", name);

                    UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                    userDAO.getConnection(pool);
                    List<User> doctors = userDAO.getAllOfType(UserRole.DOCTOR);
                    Map<Integer, String> doctorNames = new HashMap<>();

                    for (User doctor : doctors) {
                        if (doctor != null) {
                            doctorNames.put(doctor.getId(), doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic());
                        }
                    }
                    userDAO.releaseConnection(pool);
                    request.setAttribute("doctor_names", doctorNames);

                    PrescriptionDAO prescriptionDAO = (PrescriptionDAO) DAOFactory.getDAO(DAOType.PRESCRIPTION_DAO);
                    prescriptionDAO.getConnection(pool);
                    Map<Integer, List<Prescription>> prescriptionsByCaseId = new HashMap<>();

                    for (Case cur : cases) {
                        int currenctCaseId = cur.getId();
                        List<Prescription> prescriptions = prescriptionDAO.getAllByCaseId(currenctCaseId);
                        prescriptionsByCaseId.put(currenctCaseId, prescriptions);
                    }

                    prescriptionDAO.releaseConnection(pool);
                    request.setAttribute("prescriptions_by_case_id", prescriptionsByCaseId);
                    request.removeAttribute("patient");
                    request.removeAttribute("card");
                    request.removeAttribute("last_case");
                    logger.info("preparing profile page finished");
                    page = Pages.VIEW_HISTORY;
                }
            } catch (CaseDAOException | PrescriptionDAOException | UserDAOException e) {
                logger.warn(e.getMessage());
            }
        }
        return page;
    }
}
