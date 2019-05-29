package by.epam.javatraining.webproject.model.dao.implementation;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.dao.IAppointmentDAO;
import by.epam.javatraining.webproject.model.dao.exception.AppointmentDAOException;
import by.epam.javatraining.webproject.model.dao.exception.DAOException;
import by.epam.javatraining.webproject.model.entity.Appointment;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.util.Fields;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO extends AbstractDAO implements IAppointmentDAO {

    private static final String INSERT_APPOINTMENT = "INSERT INTO appointment VALUES(default, ?, ?, ?, ?)";
    private static final String GET_ALL_BY_PATIENT_ID_APPOINTMENT = "SELECT idappointment, iddoctor, idpatient, active, appointment.date FROM "
            + "appointment WHERE idpatient=?";
    private static final String GET_ALL_BY_DOCTOR_ID_APPOINTMENT = "SELECT idappointment, iddoctor, idpatient, active, appointment.date FROM "
            + "appointment WHERE iddoctor=?";
    private static final String GET_BY_ID = "SELECT idappointment, iddoctor, idpatient, active, appointment.date FROM "
            + "appointment WHERE idappointment=?";
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE idappointment = ?";

    @Override
    public List getAll() throws AppointmentDAOException {
        return null;
    }

    @Override
    public Appointment getById(int id) throws AppointmentDAOException {

        Appointment appointment = null;
        if (id != 0) {

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
                preparedStatement.setInt(1, id);
                List<Appointment> appointments = unmarshal(preparedStatement.executeQuery());

                if (appointments != null && !appointments.isEmpty()) {
                    appointment = appointments.get(0);
                }
            } catch (SQLException e) {
                logger.error("appointmentDAO: " + e.getMessage());
            }
        }
        return appointment;
    }

    @Override
    public boolean insert(Entity entity) throws AppointmentDAOException {
        if (entity instanceof Appointment) {
            Appointment appointment = (Appointment) entity;
            try {
                logger.debug("started executing insertion of appointment");
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_APPOINTMENT);
                preparedStatement.setInt(1, appointment.getDoctorId());
                preparedStatement.setInt(2, appointment.getPatientId());
                preparedStatement.setBoolean(3, appointment.getActive());
                preparedStatement.setString(4, appointment.getDate());
                preparedStatement.executeUpdate();
                logger.debug("finished executing insertion of appointment");
                return true;
            } catch (SQLException e) {
                throw new AppointmentDAOException("Exception inserting appointment into database: " + e);
            }
        }
        return false;
    }

    @Override
    public boolean delete(Entity entity) throws AppointmentDAOException {
        boolean result = false;

        if (entity != null) {
            Appointment appointment = (Appointment) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_APPOINTMENT);
                preparedStatement.setInt(1, appointment.getId());
                preparedStatement.executeUpdate();
                logger.debug("deleting appointment " + appointment.getId() + " was successful");

            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new AppointmentDAOException("could not delete appointment: " + e.getMessage());
            }
            result = true;
        } else {
            logger.error("entity was null");
        }
        return result;
    }

    @Override
    public boolean update(Entity entity) throws AppointmentDAOException {
        return false;
    }

    @Override
    public List<Appointment> getAllByPatientId(int id) throws AppointmentDAOException {
        try {
            return getAllByIntParameter(GET_ALL_BY_PATIENT_ID_APPOINTMENT, id);
        } catch (SQLException e) {
            throw new AppointmentDAOException("Exception getting appointment from database: " + e);
        }
    }

    public List<Appointment> getAllByDoctorId(int id) throws AppointmentDAOException {
        try {
            return getAllByIntParameter(GET_ALL_BY_DOCTOR_ID_APPOINTMENT, id);
        } catch (SQLException e) {
            throw new AppointmentDAOException("Exception getting appointment from database: " + e);
        }
    }

    private List<Appointment> getAllByIntParameter(String query, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        return unmarshal(preparedStatement.executeQuery());
    }

    private List<Appointment> unmarshal(ResultSet resultSet) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        while (resultSet.next()) {
            int appointmentId = resultSet.getInt(Fields.IDAPPOINTMENT);
            int doctorId = resultSet.getInt(Fields.IDDOCTOR);
            int patientId = resultSet.getInt(Fields.IDPATIENT);
            boolean active = resultSet.getBoolean(Fields.ACTIVE);
            String date = resultSet.getString(Fields.DATE);
            Appointment appointment = new Appointment(appointmentId, doctorId, patientId, active, date);
            appointments.add(appointment);
            logger.debug("got app. from DB: " + appointment);
        }
        return appointments;
    }
}
