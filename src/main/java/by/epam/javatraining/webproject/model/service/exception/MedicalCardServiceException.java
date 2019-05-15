package by.epam.javatraining.webproject.model.service.exception;

public class MedicalCardServiceException extends ServiceException {

    public MedicalCardServiceException(String message) {
    }

    public MedicalCardServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MedicalCardServiceException(Throwable cause) {
        super(cause);
    }

}
