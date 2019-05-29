package by.epam.javatraining.webproject.model.dao.exception;

public class AppointmentDAOException extends DAOException {
    public AppointmentDAOException() {
    }

    public AppointmentDAOException(String message) {
        super(message);
    }

    public AppointmentDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentDAOException(Throwable cause) {
        super(cause);
    }
}
