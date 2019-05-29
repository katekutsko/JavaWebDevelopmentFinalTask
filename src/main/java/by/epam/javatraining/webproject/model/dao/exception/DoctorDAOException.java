package by.epam.javatraining.webproject.model.dao.exception;

public class DoctorDAOException extends DAOException {

    public DoctorDAOException() {
    }

    public DoctorDAOException(String message) {
        super(message);
    }

    public DoctorDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoctorDAOException(Throwable cause) {
        super(cause);
    }
}
