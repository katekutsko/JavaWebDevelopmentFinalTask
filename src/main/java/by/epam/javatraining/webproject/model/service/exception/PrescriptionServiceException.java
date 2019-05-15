package by.epam.javatraining.webproject.model.service.exception;

public class PrescriptionServiceException extends ServiceException {

    public PrescriptionServiceException(String message) {
        super(message);
    }

    public PrescriptionServiceException() {
    }

    public PrescriptionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrescriptionServiceException(Throwable cause) {
        super(cause);
    }
}
