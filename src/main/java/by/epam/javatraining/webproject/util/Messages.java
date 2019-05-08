package by.epam.javatraining.webproject.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    public static final String FIELDS_NOT_FILLED = "fields_not_filled";
    public static final String WRONG_PASSWORD = "wrong_password";
    public static final String WRONG_LOGIN = "wrong_login";
    public static final String ACTION_NOT_PERFORMED = "action_not_performed";
    public static final String REGISTRATION_FAILED = "registration_failed";
    public static final String LOCALISATION_PROPERTIES = "localisation";
    public static final String PROFILE_NOT_UPDATED = "profile_not_updated";
    public static final String NO_RESULTS = "no_results";
    public static final String CONNECTION_ERROR = "something_went_wrong";

    private static ResourceBundle resourceBundle;
    private static Locale locale;

    static {
        locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle(LOCALISATION_PROPERTIES, locale);
    }

    public static void changeLocale(Locale newLocale){
        locale = newLocale;
        resourceBundle = ResourceBundle.getBundle(LOCALISATION_PROPERTIES, locale);
    }

    public static String getString(String key){

        if (key != null) {
            return resourceBundle.getString(key);
        }
        return null;
    }

}
