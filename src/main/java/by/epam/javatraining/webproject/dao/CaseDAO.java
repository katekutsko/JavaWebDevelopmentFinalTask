package by.epam.javatraining.webproject.dao;

import by.epam.javatraining.webproject.entity.Case;
import by.epam.javatraining.webproject.entity.Diagnosis;
import by.epam.javatraining.webproject.entity.Entity;
import by.epam.javatraining.webproject.exception.CaseDAOException;
import by.epam.javatraining.webproject.exception.InvalidMedicalCardException;
import by.epam.javatraining.webproject.exception.UserExtractingException;
import by.epam.javatraining.webproject.util.configuration.ConfigurationData;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaseDAO extends AbstractDAO implements ICaseDAO {

    private Logger logger;

    {
        logger = Logger.getRootLogger();
        DOMConfigurator.configure(ConfigurationData.getString(ConfigurationData.LOG4J_XML));
    }

    private static final String FIND_ALL = "SELECT idcase, admission_date, complaints, discharge_date, final_diagnosis," +
            "idmedical_card, active, iddoctor FROM hospital.case";

    private static final String FIND_BY_ID = FIND_ALL + " WHERE idcase = ?";

    private static final String FIND_BY_MEDICAL_CARD_ID = FIND_ALL + " WHERE idmedical_card = ?";

    private static final String CLOSE_CASE = "UPDATE hospital.case SET final_diagnosis = ?, discharge_date = ?, " +
            "active = 0 WHERE idcase = ?";

    private static final String UPDATE_CASE = "UPDATE hospital.case SET admission_date = ?, complaints = ?, " +
            "final_diagnosis = ?, discharge_date = ? WHERE idcase = ?";

    private static final String INSERT_CASE = "INSERT INTO hospital.case(admission_date," +
            " complaints, idmedical_card, iddoctor, active) VALUES(?, ?, ?, ?, 1)";

    private static final String FIND_LAST_CASE_OF_A_PATIENT = "SELECT * FROM hospital.case WHERE idmedical_card = ? ORDER BY idcase DESC LIMIT 1";

    private static final String DELETE_CASE = "DELETE FROM hospital.case WHERE idcase = ?";

    @Override
    public List getAll() throws CaseDAOException {
        return null;
    }

    @Override
    public Entity getById(int id) throws CaseDAOException {
        try {
            return getCaseByIntParameter(id, FIND_BY_ID);
        } catch (SQLException e) {
            logger.error("could not extract case by id: " + e.getMessage());
            throw new CaseDAOException("could not extract case by id: " + e.getMessage());
        }
    }

    private List<Case> unmarshal(ResultSet resultSet) throws SQLException {

        if (resultSet != null) {
            List<Case> caseList = new ArrayList<>();

            while (resultSet.next()) {
                Case foundCase = new Case();
                foundCase.setId(resultSet.getInt("idcase"));
                foundCase.setAdmissionDate(resultSet.getString("admission_date"));
                foundCase.setDischargeDate(resultSet.getString("discharge_date"));
                foundCase.setActive(resultSet.getInt("active"));
                foundCase.setDoctorId(resultSet.getInt("iddoctor"));
                int diagnosisId = resultSet.getInt("final_diagnosis");

                if (diagnosisId != 0) {
                    foundCase.setFinalDiagnosis(Diagnosis.values()[diagnosisId - 1]);
                } else {
                    foundCase.setFinalDiagnosis(null);
                }
                foundCase.setComplaints(resultSet.getString("complaints"));
                foundCase.setMedicalCardId(resultSet.getInt("idmedical_card"));
                logger.debug("found case: " + foundCase);
                caseList.add(foundCase);
            }
            return caseList;
        }
        logger.debug("result set is null");
        return null;
    }

    @Override
    public boolean insert(Entity entity) throws CaseDAOException {

        if (entity instanceof Case) {
            Case newCase = (Case) entity;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CASE);
                preparedStatement.setString(1, newCase.getAdmissionDate());
                preparedStatement.setString(2, newCase.getComplaints());
                preparedStatement.setInt(3, newCase.getMedicalCardId());
                preparedStatement.setInt(4, newCase.getDoctorId());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new CaseDAOException("Exception inserting case into database" + e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Entity entity) {
       boolean result = false;

       if (entity != null) {
            Case deletedCase = (Case) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CASE);
                preparedStatement.setInt(1, deletedCase.getId());
                preparedStatement.executeUpdate();
                logger.debug("deleting  case " + deletedCase.getId() + " was successful");

            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            result = true;
        }
        return result;
    }

    @Override
    public boolean update(Entity entity) throws CaseDAOException {

        if (entity instanceof Case) {
            Case updatedCase = (Case) entity;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CASE);
                preparedStatement.setString(1, updatedCase.getAdmissionDate());
                preparedStatement.setString(2, updatedCase.getComplaints());
                preparedStatement.setInt(3, updatedCase.getFinalDiagnosis().ordinal() + 1);
                preparedStatement.setString(4, updatedCase.getDischargeDate());
                preparedStatement.setInt(5, updatedCase.getId());

                if (preparedStatement.executeUpdate() != 0) {
                    logger.info(updatedCase + " was updated in database");
                    return true;
                }
            } catch (SQLException e) {
                throw new CaseDAOException("Exception updating case in database" + e);
            }
        }
        return false;
    }

    private Case getCaseByIntParameter(int id, String query) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Case> cases = unmarshal(resultSet);

        if (cases != null && !cases.isEmpty()) {
            logger.debug("case list is NOT empty and it's size is " + cases.size());
            return cases.get(0);
        }
        logger.debug("case list is null");
        return null;
    }

    public Case getLastCaseByPatientId(int patientId) throws CaseDAOException {
        try {
            return getCaseByIntParameter(patientId, FIND_LAST_CASE_OF_A_PATIENT);
        } catch (SQLException e) {
            throw new CaseDAOException("could not extract case by patient id: " + e.getMessage());
        }
    }

    public boolean closeCase(Entity entity) throws CaseDAOException {

        if (entity instanceof Case) {
            Case updatedCase = (Case) entity;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(CLOSE_CASE);
                preparedStatement.setInt(1, updatedCase.getFinalDiagnosis().ordinal() + 1);
                preparedStatement.setString(2, updatedCase.getDischargeDate());
                preparedStatement.setInt(3, updatedCase.getId());

                if (preparedStatement.executeUpdate() != 0) {
                    logger.info(updatedCase + " was closed");
                    return true;
                }
            } catch (SQLException e) {
                throw new CaseDAOException("Exception closing case" + e);
            }
        }
        return false;
    }

    public List<Case> getAllCasesOfCertainPatient(int medicalCardId) throws CaseDAOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_MEDICAL_CARD_ID);
            preparedStatement.setInt(1, medicalCardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return unmarshal(resultSet);

        } catch (SQLException e) {
            throw new CaseDAOException();
        }
    }

}
