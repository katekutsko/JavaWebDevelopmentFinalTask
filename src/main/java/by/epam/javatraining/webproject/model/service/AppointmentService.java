package by.epam.javatraining.webproject.model.service;

import by.epam.javatraining.webproject.model.dao.exception.AppointmentDAOException;
import by.epam.javatraining.webproject.model.dao.exception.DAOException;
import by.epam.javatraining.webproject.model.dao.factory.DAOFactory;
import by.epam.javatraining.webproject.model.dao.factory.DAOType;
import by.epam.javatraining.webproject.model.dao.implementation.AppointmentDAO;
import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.service.exception.AppointmentServiceException;
import by.epam.javatraining.webproject.model.service.exception.MedicalCardServiceException;
import by.epam.javatraining.webproject.model.service.factory.ServiceFactory;
import by.epam.javatraining.webproject.model.service.factory.ServiceType;
import by.epam.javatraining.webproject.model.validation.AppointmentValidation;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService extends Service {

    private AppointmentDAO appointmentDAO;

    {
        appointmentDAO = (AppointmentDAO) DAOFactory.getDAO(DAOType.APPOINTMENT_DAO);
        dao = appointmentDAO;
    }

    @Override
    public boolean add(Entity entity) throws AppointmentServiceException {

        boolean result = false;
        try {
            if (entity instanceof Appointment) {
                Appointment appointment = (Appointment) entity;
                if (AppointmentValidation.validateAppointment(appointment)) {
                    result = appointmentDAO.insert(entity);
                }
            }
        } catch (AppointmentDAOException e) {
            throw new AppointmentServiceException(e.getMessage());
        }
        return result;
    }

    @Override
    public Appointment getById(int id) {

        Appointment appointment = null;
        try {
            appointment = appointmentDAO.getById(id);
        } catch (AppointmentDAOException e) {
            logger.error(e.getMessage());
        }
        return appointment;
    }

    public List<Appointment> getAllOfPatient(int cardId) throws AppointmentServiceException {
        try {
            return appointmentDAO.getAllByPatientId(cardId);
        } catch (AppointmentDAOException e) {
            logger.error(e.getMessage());
            throw new AppointmentServiceException(e.getMessage());
        }
    }

    public boolean delete(Appointment appointment) throws AppointmentServiceException {
        try {
            return appointmentDAO.delete(appointment);
        } catch (AppointmentDAOException e) {
            logger.error(e.getMessage());
            throw new AppointmentServiceException(e.getMessage());
        }
    }

    public List<Appointment> getAllOfDoctor(int id) throws AppointmentServiceException {
        try {
            return appointmentDAO.getAllByDoctorId(id);
        } catch (AppointmentDAOException e) {
            logger.error(e.getMessage());
            throw new AppointmentServiceException(e.getMessage());
        }
    }

    public List<Appointment> selectActive(List<Appointment> appointments) {
        List<Appointment> activeAppointments = new ArrayList<>();
        for (Appointment appointment : appointments){
            if (appointment.getActive()){
                activeAppointments.add(appointment);
            }
        }
        return activeAppointments;
    }
}
