package by.epam.javatraining.webproject.exception;

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
