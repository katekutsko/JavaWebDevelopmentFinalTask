package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.dao.DAOFactory;
import by.epam.javatraining.webproject.dao.DAOType;
import by.epam.javatraining.webproject.dao.MedicalCardDAO;
import by.epam.javatraining.webproject.dao.UserDAO;
import by.epam.javatraining.webproject.dao.connection.ConnectionPool;
import by.epam.javatraining.webproject.entity.MedicalCard;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.CommitException;
import by.epam.javatraining.webproject.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Messages;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.validation.RegistrationValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class EditProfileCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = null;

        if (type == ActionType.GET) {
            page = Pages.FORWARD_EDIT_PROFILE;

        } else {
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String patronymic = request.getParameter("patronymic");
                String login = request.getParameter("login");
                String oldPassword = request.getParameter("old_password");
                String newPassword = request.getParameter("new_password");
                String repeatNewPassword = request.getParameter("repeat_new_password");

                if (RegistrationValidation.validateNameComponent(name)) {
                    user.setName(name);
                }
                if (RegistrationValidation.validateNameComponent(surname)) {
                    user.setSurname(surname);
                }
                if (RegistrationValidation.validateNameComponent(patronymic)) {
                    user.setPatronymic(patronymic);
                }
                if (RegistrationValidation.validateLogin(login)) {
                    user.setLogin(login);
                }
                if (!oldPassword.equals("") && !newPassword.equals("") && !repeatNewPassword.equals("")) {
                    logger.debug(user.getPassword() + "(" + Long.parseLong(oldPassword) + ") " + " - old encoded password, " + newPassword + " (" + repeatNewPassword + ") - new one");

                    if (Long.parseLong(oldPassword) == user.getPassword()) {

                        if (RegistrationValidation.validatePassword(newPassword) && RegistrationValidation.validateRepeatPassword(newPassword,
                                repeatNewPassword)) {
                            user.setPassword(newPassword.hashCode());
                        }
                    }
                }
                UserDAO userDAO = (UserDAO) DAOFactory.getDAO(DAOType.USER_DAO);
                ConnectionPool pool = ConnectionPool.getInstance();
                userDAO.getConnection(pool);

                try {
                    userDAO.setAutoCommit(false);
                    if (userDAO.update(user)) {
                        logger.info("user successfully updated: " + user);

                        if (user.getRole() == UserRole.PATIENT) {
                            String dateOfBirth = request.getParameter("birth_date");
                            String sex = request.getParameter("sex");

                            MedicalCardDAO cardDAO = (MedicalCardDAO) DAOFactory.getDAO(DAOType.MEDICAL_CARD_DAO);
                            cardDAO.getConnection(pool);
                            MedicalCard card = cardDAO.getByPatientId(user.getId());

                            card.setDateOfBirth(dateOfBirth);
                            card.setSex(Byte.parseByte(sex));

                            if (cardDAO.update(card)) {
                                request.getSession().removeAttribute("user");
                                request.getSession().removeAttribute("card");
                                request.getSession().setAttribute("user", user);
                                request.getSession().setAttribute("card", card);
                                logger.info("card updated: " + card);
                            } else {
                                userDAO.rollback();
                            }
                            cardDAO.releaseConnection(pool);
                        } else {
                            request.getSession().removeAttribute("user");
                            request.getSession().setAttribute("user", user);
                        }
                        userDAO.setAutoCommit(true);
                        userDAO.releaseConnection(pool);
                    }
                    page = Pages.REDIRECT_VIEW_PROFILE;
                } catch (UserDAOException | CommitException e) {
                    request.setAttribute("errorMessage", Messages.getString(Messages.PROFILE_NOT_UPDATED));
                }
            }
        }
        return page;
    }
}
