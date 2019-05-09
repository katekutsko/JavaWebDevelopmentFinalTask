package by.epam.javatraining.webproject.model.exception;

public class CaseDAOException extends DAOException{

    public CaseDAOException() {
    }

    public CaseDAOException(String message) {
        super(message);
    }

    public CaseDAOException(Throwable cause) {
        super(cause);
    }

    public CaseDAOException(String message, Throwable e) {
    }
}
