package by.epam.javatraining.webproject.model.dao;

import by.epam.javatraining.webproject.model.dao.exception.AppointmentDAOException;
import by.epam.javatraining.webproject.model.entity.Appointment;

import java.util.List;

public interface IAppointmentDAO extends IDAO {
    List<Appointment> getAllByPatientId(int id) throws AppointmentDAOException;
}
