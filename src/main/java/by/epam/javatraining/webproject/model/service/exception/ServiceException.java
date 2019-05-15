package by.epam.javatraining.webproject.model.service.exception;

public class ServiceException extends Exception {
    public ServiceException(String message) {
    }

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
