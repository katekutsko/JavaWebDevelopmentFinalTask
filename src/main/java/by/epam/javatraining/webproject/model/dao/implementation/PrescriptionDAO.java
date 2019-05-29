package by.epam.javatraining.webproject.model.dao.implementation;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.dao.IPrescriptionDAO;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.Prescription;
import by.epam.javatraining.webproject.model.entity.prescriptiontype.PrescriptionType;
import by.epam.javatraining.webproject.model.dao.exception.PrescriptionDAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO extends AbstractDAO implements IPrescriptionDAO {

    private static final String INSERT_PRESCRIPTION = "INSERT INTO prescription VALUES(default, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL = "SELECT * FROM prescription";

    private static final String FIND_BY_ID = SELECT_ALL + " WHERE idprescription = ?";

    private static final String SELECT_ALL_BY_CASE_ID = SELECT_ALL + " WHERE idcase = ?";

    private static final String SELECT_ALL_BY_DOCTOR_ID = SELECT_ALL + " WHERE iddoctor = ?";

    private static final String SELECT_ALL_BY_PATIENT_ID = SELECT_ALL + " WHERE idcard = ?";

    private static final String DELETE_PRESCRIPTION = "DELETE FROM prescription WHERE idprescription = ?";

    @Override
    public List getAll() throws PrescriptionDAOException {
        return null;
    }

    @Override
    public Entity getById(int id) throws PrescriptionDAOException {
        return null;
    }

    @Override
    public List<Prescription> getAllByCaseId(int currentCaseId) throws PrescriptionDAOException {
        try {
        return getByIntParameter(currentCaseId, SELECT_ALL_BY_CASE_ID);
        } catch (SQLException e) {
            throw new PrescriptionDAOException(e.getMessage());
        }
    }

    @Override
    public List<Prescription> getByPatientId(int userId) throws PrescriptionDAOException {
        try {
            return getByIntParameter(userId, SELECT_ALL_BY_PATIENT_ID);
        } catch (SQLException e) {
            throw new PrescriptionDAOException(e.getMessage());
        }
    }

    private List<Prescription> getByIntParameter(int id, String query) throws SQLException {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return unmarshal(resultSet);
    }

    private List<Prescription> unmarshal(ResultSet resultSet) throws SQLException {

        if (resultSet != null) {
            List<Prescription> prescriptions = new ArrayList<>();

            while (resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setId(resultSet.getInt("idprescription"));
                prescription.setCaseId(resultSet.getInt("idcase"));
                prescription.setType(PrescriptionType.values()[resultSet.getInt("idprescription_type") - 1]);
                prescription.setDate(resultSet.getString("date"));
                prescription.setDetails(resultSet.getString("details"));
                prescription.setDoctorId(resultSet.getInt("iddoctor"));
                prescription.setCardId(resultSet.getInt("idcard"));
                prescriptions.add(prescription);
                logger.debug(prescription + " was added");
            }

            return prescriptions;
        }
        return null;
    }

    @Override
    public boolean insert(Entity entity) {
        if (entity instanceof Prescription) {

            Prescription prescription = (Prescription) entity;
            logger.debug(entity + " was received for insertion");
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRESCRIPTION);
                preparedStatement.setInt(1, prescription.getType().ordinal() + 1);
                preparedStatement.setString(2, prescription.getDate());
                preparedStatement.setString(3, prescription.getDetails());
                preparedStatement.setInt(4, prescription.getCaseId());
                preparedStatement.setInt(5, prescription.getDoctorId());
                preparedStatement.setInt(6, prescription.getCardId());
                preparedStatement.execute();

                return true;

            } catch (SQLException e) {

                return false;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Entity entity) {
        boolean result = false;

        if (entity != null) {
            Prescription prescription = (Prescription) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRESCRIPTION);
                preparedStatement.setInt(1, prescription.getId());
                preparedStatement.executeUpdate();
                logger.debug("deleting  prescription " + prescription.getId() + " was successful");

            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            result = true;
        }
        return result;
    }

    @Override
    public boolean update(Entity entity) throws PrescriptionDAOException {
        return false;
    }
}
