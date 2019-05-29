package by.epam.javatraining.webproject.model.validation;

import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.UserService;
import by.epam.javatraining.webproject.model.service.exception.UserServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.util.Messages;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates input data during registration and log in process.
 *
 * @author Kutsko Katsiaryna
 * @version 1.0 25.05.19
 *
 */
public class UserDataValidation {

    private final static int VALIDATION_PARAMETERS_AMOUNT = 5;

    private static final int NAME_ERROR_INDEX = 0;
    private static final int PASSWORD_ERROR_INDEX = 1;
    private static final int PASSWORDS_DIFFER_ERROR_INDEX = 2;
    private static final int LOGIN_ERROR_INDEX = 3;
    private static final int PHONE_NUMBER_ERROR_INDEX = 4;

    private static final String NAME_PATTERN = "[А-Я][А-Яа-я]{1,20}";
    private static final String LOGIN_PATTERN = "^(?=.+\\@\\w{4,6}\\.\\w{2,3}).{10,20}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9].*[0-9]).*.{6,10}$";
    private static final String PHONE_NUMBER_PATTERN = "\\+\\d{3}-\\d{2}-\\d{3}-\\d{2}-\\d{2}";
    private static final String OFFICE_NUMBER_PATTERN = "\\d{3}";

    private static final Logger logger;

    static {
        logger = Logger.getRootLogger();
    }

    private static boolean validateNameComponent(String string) {
        return string.matches(NAME_PATTERN);
    }

    private static boolean validateLogin(String string) {
        return string.matches(LOGIN_PATTERN);
    }

    private static boolean validatePassword(String string) {
        return string.matches(PASSWORD_PATTERN);
    }

    private static boolean validateRepeatPassword(String string, String repeat) {
        return string.equals(repeat);
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    private static boolean validateOfficeNumber(String officeNumberAsString) {
        return officeNumberAsString.matches(OFFICE_NUMBER_PATTERN);
    }

    /**
     * Checks if any fields were skipped
     *
     * @param name First name of the user
     * @param surname Last name of the user
     * @param patronymic Patronymic of the user
     * @param password Password in format of string
     * @param login Login of the user, must be a valid e-mail address
     * @param phoneNumber Phone number of the user, must be belorussian
     * @param sex Sex of the user
     * @param birthDate Birth date of the user
     * @return <code>true</code> when at least one of the fields was empty, <code>false</code> otherwise
     */
    public static boolean lookForEmptyFields(String name, String surname, String patronymic, String password,
                                             String login, String phoneNumber, String sex, String birthDate){

        boolean emptyFieldPresent = (name.equals("") || surname.equals("") || patronymic.equals("") || password.equals("") || login.equals("") || phoneNumber.equals(""));
        if (!emptyFieldPresent){
            emptyFieldPresent = (sex != null && sex.isEmpty()) && (birthDate != null && birthDate.isEmpty());
        }
        logger.debug("data for empty field validation: " + name + surname + patronymic + password + login + phoneNumber + sex + birthDate);
        return emptyFieldPresent;
    }

    /**
     * Forms error messages list during registration
     *
     * @param name First name of the user
     * @param surname Last name of the user
     * @param patronymic Patronymic of the user
     * @param password Password in format of string
     * @param login Login of the user, must be a valid e-mail address
     * @param phoneNumber Phone number of the user, must be belorussian
     * @return <code>null</code> when no invalid data was found, list of error messages otherwise
     */
    public static List<String> formRegistrationErrorMessage(String name, String surname, String patronymic, String password,
                                                            String repeatPassword, String login, String phoneNumber) {

        boolean[] check = new boolean[VALIDATION_PARAMETERS_AMOUNT];
        check[NAME_ERROR_INDEX] = (UserDataValidation.validateNameComponent(name)
                && UserDataValidation.validateNameComponent(surname) && UserDataValidation.validateNameComponent(patronymic));
        check[PASSWORD_ERROR_INDEX] = UserDataValidation.validatePassword(password);
        check[PASSWORDS_DIFFER_ERROR_INDEX] = UserDataValidation.validateRepeatPassword(repeatPassword, password);
        check[LOGIN_ERROR_INDEX] = UserDataValidation.validateLogin(login);
        check[PHONE_NUMBER_ERROR_INDEX] = UserDataValidation.validatePhoneNumber(phoneNumber);

        String[] errorMessages = new String[VALIDATION_PARAMETERS_AMOUNT];
        errorMessages[NAME_ERROR_INDEX] = Messages.INVALID_NAME;
        errorMessages[PASSWORD_ERROR_INDEX] = Messages.INVALID_PASSWORD;
        errorMessages[PASSWORDS_DIFFER_ERROR_INDEX] = Messages.INCONSISTENT_PASSWORDS;
        errorMessages[LOGIN_ERROR_INDEX] = Messages.INVALID_LOGIN;
        errorMessages[PHONE_NUMBER_ERROR_INDEX] = Messages.INVALID_PHONE_NUMBER;


        List<String> errors = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < VALIDATION_PARAMETERS_AMOUNT; i++) {
            if (!check[i]) {
                logger.info(errorMessages[i]);
                errors.add(errorMessages[i]);
                count++;
            } else {
                errors.add(null);
            }
        }
        return count == 0 ? null : errors;
    }

    /**
     * Forms error messages list during editing profile
     *
     * @param name First name of the user
     * @param surname Last name of the user
     * @param patronymic Patronymic of the user
     * @param oldPassword Old password in format of string
     * @param newPassword New password
     * @param repeatNewPassword Repeated new password
     * @param login Login of the user, must be a valid e-mail address
     * @param phoneNumber Phone number of the user, must be belorussian
     * @return <code>null</code> when no invalid data was found, list of error messages otherwise
     */
    public static List<String> formEditProfileErrorMessage(User user, String name, String surname, String patronymic, String oldPassword,
                                                           String newPassword, String repeatNewPassword, String login, String phoneNumber) {

        List<String> errors = formRegistrationErrorMessage(name, surname, patronymic, newPassword, repeatNewPassword, login, phoneNumber);

        if (errors != null) {
            if (isPasswordSkipped(oldPassword, newPassword, repeatNewPassword)) {
                errors.set(PASSWORD_ERROR_INDEX, null);
                errors.set(PASSWORDS_DIFFER_ERROR_INDEX, null);

                int count = 0;
                for (String error : errors) {
                    if (error != null) {
                        break;
                    }
                    count++;
                }
                if (count == errors.size()) {
                    errors = null;
                }
            }
        } else {
            boolean correctPassword = comparePasswords(user.getPassword(), newPassword.hashCode());
            boolean uniqueLogin = false;

            if (!user.getLogin().equals(login)) {
                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                uniqueLogin = !userService.checkLoginUniqueness(user, login);
            }

            if (!(correctPassword && uniqueLogin)) {
                errors = new ArrayList<>();
                for (int i = 0; i < VALIDATION_PARAMETERS_AMOUNT; i++) {
                    errors.add(null);
                }
                if (correctPassword) {
                    errors.add(null);
                    logger.debug(Messages.LOGIN_EXISTS);
                    errors.add(Messages.LOGIN_EXISTS);
                } else {
                    logger.debug(Messages.INVALID_PASSWORD);
                    errors.add(Messages.INVALID_PASSWORD);
                }
            }
        }
        return errors;
    }

    /**
     * Checks if password change was skipped.
     *
     * @param oldPassword Old password in format of string
     * @param newPassword New password
     * @param repeatNewPassword Repeated new password to avoid mistakes
     * @return <code>true</code> when password change was not attempted, <code>false</code> otherwise
     */
    private static boolean isPasswordSkipped(String oldPassword, String newPassword, String repeatNewPassword) {
        return oldPassword.equals("") && newPassword.equals("") && repeatNewPassword.equals("");
    }

    /**
     * Compares real and entered password hashes.
     *
     * @param oldPasswordHash Old password hashcode
     * @param newPasswordHash New password hashcode
     * @return <code>true</code> when passwords match, <code>false</code> otherwise
     */
    private static boolean comparePasswords(int oldPasswordHash, int newPasswordHash) {
        return oldPasswordHash == newPasswordHash;
    }
}
