package by.epam.javatraining.webproject.model.service.factory;

import by.epam.javatraining.webproject.model.service.Service;

public class ServiceFactory {

    public static Service getService(ServiceType serviceType){
        return serviceType.getService();
    }
}
