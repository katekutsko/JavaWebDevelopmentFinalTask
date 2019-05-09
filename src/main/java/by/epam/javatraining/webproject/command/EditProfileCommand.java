package by.epam.javatraining.webproject.command;

import by.epam.javatraining.webproject.controller.ActionType;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.MedicalCardService;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Pages;
import by.epam.javatraining.webproject.model.validation.RegistrationValidation;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class EditProfileCommand implements Command {
    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public String execute(HttpServletRequest request, ActionType type) {
        String page = null;

        HttpSession session = request.getSession();
        if (type == ActionType.GET) {
            page = Pages.FORWARD_EDIT_PROFILE;

        } else {
            User user = (User) session.getAttribute("user");

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

                    if (oldPassword.hashCode() == user.getPassword()) {

                        if (RegistrationValidation.validatePassword(newPassword) && RegistrationValidation.validateRepeatPassword(newPassword,
                                repeatNewPassword)) {
                            user.setPassword(newPassword.hashCode());
                        }
                    }
                }
                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                userService.getConnection();

                userService.setAutoCommit(false);

                if (userService.update(user)) {
                    logger.info("user successfully updated: " + user);

                    if (user.getRole() == UserRole.PATIENT) {
                        String dateOfBirth = request.getParameter("birth_date");
                        String sex = request.getParameter("sex");

                        MedicalCardService cardService = (MedicalCardService) ServiceFactory.getService(ServiceType.MEDICAL_CARD_SERVICE);
                        MedicalCard card = cardService.getByPatientId(user.getId());

                        if (card != null) {
                            card.setDateOfBirth(dateOfBirth);
                            card.setSex(Byte.parseByte(sex));

                            if (cardService.update(card)) {
                                session.removeAttribute("user");
                                session.removeAttribute("card");
                                session.setAttribute("user", user);
                                session.setAttribute("card", card);
                                logger.info("card updated: " + card);
                            } else {
                                userService.rollback();
                            }
                        }
                        cardService.releaseConnection();
                    } else {
                        session.removeAttribute("user");
                        session.setAttribute("user", user);
                    }
                    userService.setAutoCommit(true);
                    userService.releaseConnection();
                }
                page = Pages.REDIRECT_VIEW_PROFILE;
            }
        }
        return page;
    }
}
