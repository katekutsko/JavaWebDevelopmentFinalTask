package by.epam.javatraining.webproject.model.validation;

import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import org.apache.log4j.Logger;

public class RegistrationValidation {

    private static  Logger logger;

    static {
        logger = Logger.getRootLogger();
    }


    public static final String NAME_PATTERN = "[А-ЯA-Z][А-Яа-яA-Za-z]{1,20}";
    public static final String LOGIN_PATTERN = "^(?=.+\\@\\w{4,6}\\.\\w{2,3}).{10,20}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9].*[0-9]).*.{6,10}$";

    public static boolean validateNameComponent(String string){
        return string.matches(NAME_PATTERN);
    }

    public static boolean validateLogin(String string){
        return string.matches(LOGIN_PATTERN);
    }

    public static boolean checkLoginUniqueness(User sessionUser, UserService userService, String login) {
        boolean result = false;
        try {
            User user = userService.login(login);
            if (user != null && user.equals(sessionUser)){
                logger.debug("comparing users: "  + user + " " + sessionUser);
                result = true;
            }
        } catch (UserServiceException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public static boolean validatePassword(String string){
        return string.matches(PASSWORD_PATTERN);
    }

    public static boolean validateRepeatPassword(String string, String repeat){
        return string.equals(repeat);
    }
}
