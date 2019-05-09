package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.CaseDAO;
import by.epam.javatraining.webproject.model.dao.implementation.MedicalCardDAO;
import by.epam.javatraining.webproject.model.dao.implementation.PrescriptionDAO;
import by.epam.javatraining.webproject.model.dao.implementation.UserDAO;
import by.epam.javatraining.webproject.model.entity.Case;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.exception.*;
import by.epam.javatraining.webproject.util.Pages;
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

        String page = Pages.ERROR_PAGE;

        if (type == ActionType.POST) {

            String id = request.getParameter("user_id");
            logger.debug("id of user being deleted is " + id);

            if (id != null){
                UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                ConnectionPool pool = ConnectionPool.getInstance();
                userDAO.getConnection(pool);
                int userId = Integer.parseInt(id);

                MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
                PrescriptionDAO prescriptionDAO = (PrescriptionDAO) DAOFactory.getDAO(DAOType.PRESCRIPTION_DAO);
                CaseDAO caseDAO = (CaseDAO) DAOFactory.getDAO(DAOType.CASE_DAO);

                try {
                    User user = (User) userDAO.getById(userId);

                    if (user != null){

                        if (user.getRole() == UserRole.PATIENT) {

                            userDAO.setAutoCommit(false);
                            userDAO.delete(user);

                            cardDAO.getConnection(pool);
                            cardDAO.setAutoCommit(false);
                            caseDAO.getConnection(pool);
                            caseDAO.setAutoCommit(false);
                            prescriptionDAO.getConnection(pool);
                            prescriptionDAO.setAutoCommit(false);

                            MedicalCard card = null;
                            try {
                                card = cardDAO.getByPatientId(userId);
                            } catch (MedicalCardDAOException e) {
                                e.printStackTrace();
                            }

                            if (card != null) {

                                List<Prescription> prescriptionList = prescriptionDAO.getByPatientId(card.getId());
                                List<Case> caseList = caseDAO.getAllCasesOfCertainPatient(card.getId());

                                for (Prescription prescription : prescriptionList) {
                                    prescriptionDAO.delete(prescription);
                                    logger.debug("deleting prescription");
                                }
                                for (Case currentCase : caseList) {
                                    caseDAO.delete(currentCase);
                                    logger.debug("deleting case");
                                }
                                cardDAO.delete(card);
                                logger.debug("deleting card");

                                prescriptionDAO.commit();
                                caseDAO.commit();
                                cardDAO.commit();
                                userDAO.commit();

                                userDAO.setAutoCommit(true);
                                prescriptionDAO.setAutoCommit(true);
                                caseDAO.setAutoCommit(true);
                                cardDAO.setAutoCommit(true);
                                page = Pages.REDIRECT_VIEW_USERS;

                            }
                        } else {
                            userDAO.delete(user);
                            page = Pages.REDIRECT_VIEW_USERS;
                        }
                    }

                } catch (UserDAOException | PrescriptionDAOException | CommitException | CaseDAOException e) {
                    prescriptionDAO.rollback();
                    caseDAO.rollback();
                    cardDAO.rollback();
                    userDAO.rollback();
                    logger.error(e.getMessage());

                    page = Pages.ERROR_PAGE;

                } finally {
                    cardDAO.releaseConnection(pool);
                    caseDAO.releaseConnection(pool);
                    prescriptionDAO.releaseConnection(pool);
                    userDAO.releaseConnection(pool);
                }
            }
        }
        return page;
    }
}
