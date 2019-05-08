package by.epam.javatraining.webproject.validation;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class RegistrationValidation {

    private static  Logger logger;

    static {
        logger = Logger.getRootLogger();
    }


    public static final String NAME_PATTERN = "[А-Яа-я]{1,20}";
    public static final String LOGIN_PATTERN = "^(?=.+\\@\\w{4,6}\\.\\w{2,3}).{10,25}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9].*[0-9]).*.{6,10}$";

    public static boolean validateNameComponent(String string){
        return string.matches(NAME_PATTERN);
    }

    public static boolean validateLogin(String string){
        return string.matches(LOGIN_PATTERN);
    }

    public static boolean validatePassword(String string){
        return string.matches(PASSWORD_PATTERN);
    }

    public static boolean validateRepeatPassword(String string, String repeat){
        return string.equals(repeat);
    }
}
