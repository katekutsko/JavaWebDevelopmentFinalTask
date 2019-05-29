package by.epam.javatraining.webproject.model.service.exception;

public class AppointmentServiceException extends ServiceException {
    public AppointmentServiceException(String message) {
        super(message);
    }

    public AppointmentServiceException() {
    }

    public AppointmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentServiceException(Throwable cause) {
        super(cause);
    }
}
