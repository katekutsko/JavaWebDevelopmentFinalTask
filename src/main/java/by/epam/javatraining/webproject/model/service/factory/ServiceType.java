package by.epam.javatraining.webproject.model.service.factory;

import by.epam.javatraining.webproject.model.service.*;

public enum ServiceType {
    USER_SERVICE(new UserService()),
    MEDICAL_CARD_SERVICE(new MedicalCardService()),
    CASE_SERVICE(new CaseService()),
    PRESCRIPTION_SERVICE(new PrescriptionService()),
    APPOINTMENT_SERVICE(new AppointmentService());

    private Service service;

    ServiceType(Service service){
        this.service = service;
    }

   public Service getService(){
        return service;
   }
}
