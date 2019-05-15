package by.epam.javatraining.webproject.model.service.exception;

public class CaseServiceException extends ServiceException {
    public CaseServiceException(String message) {
    }

    public CaseServiceException() {
        super();
    }

    public CaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaseServiceException(Throwable cause) {
        super(cause);
    }
}
