package by.epam.javatraining.webproject.model.service.exception;

public class UserServiceException extends ServiceException {
    public UserServiceException() {
    }

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceException(Throwable cause) {
        super(cause);
    }
}
