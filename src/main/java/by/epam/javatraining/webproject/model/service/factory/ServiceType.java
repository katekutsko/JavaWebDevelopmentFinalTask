package by.epam.javatraining.webproject.model.service.factory;

import by.epam.javatraining.webproject.model.service.*;

public enum ServiceType {
    USER_SERVICE(new UserService()),
    MEDICAL_CARD_SERVICE(new MedicalCardService()),
    CASE_SERVICE(new CaseService()),
    PRESCRIPTION_SERVICE(new PrescriptionService());

    private Service service;

    private ServiceType(Service service){
        this.service = service;
    }

   public Service getService(){
        return service;
   }
}
