package by.epam.javatraining.webproject.util.configuration;

import java.util.ResourceBundle;

public class ConfigurationData {

    private static final ResourceBundle resourceBundle;

    public static final String LOG4J_XML = "log4j";
    public static final String DRIVER_PATH = "driver_path";
    public static final String DATABASE_LOGIN = "database_login";
    public static final String DATABASE_PASSWORD = "database_password";
    public static final String DATABASE_CONNECTION_PATH = "database_path";
    private static final String CONFIGURATION_PROPERTIES = "configuration";

    static {
        resourceBundle = ResourceBundle.getBundle(CONFIGURATION_PROPERTIES);

    }

    public static String getString(String key){

        if (key != null) {
            return resourceBundle.getString(key);
        }
        return null;
    }
}
