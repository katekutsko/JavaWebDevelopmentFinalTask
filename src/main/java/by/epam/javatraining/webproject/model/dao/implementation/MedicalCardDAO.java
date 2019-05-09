package by.epam.javatraining.webproject.model.dao.implementation;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.dao.IMedicalCardDAO;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.MedicalCard;
import by.epam.javatraining.webproject.model.exception.MedicalCardDAOException;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalCardDAO extends AbstractDAO implements IMedicalCardDAO {
    public static final String FIND_ALL = "SELECT idmedical_card, iduser, sex, birth_date FROM medical_card";
    public static final String FIND_BY_PATIENT_ID = FIND_ALL + " WHERE iduser = ?";
    public static final String FIND_BY_ID = FIND_ALL + " WHERE idmedical_card = ?";
    public static final String FIND_BY_PATIENT_FULL_NAME = FIND_ALL + " INNER JOIN user ON user.iduser = medical_card.iduser WHERE last_name = ?, " +
            "first_name = ?, patronymic = ?";
    public static final String FIND_BY_PATIENT_LAST_NAME = FIND_ALL + " INNER JOIN user ON user.iduser = medical_card.iduser WHERE last_name = ?";
    public static final String ADD_MEDICAL_CARD = "INSERT INTO medical_card(iduser, sex, birth_date) VALUES(?, ?, ?)";
    public static final String UPDATE_MEDICAL_CARD = "UPDATE medical_card SET sex = ?, birth_date = ? WHERE idmedical_card = ?";
    public static final String DELETE_MEDICAL_CARD = "DELETE FROM medical_card WHERE idmedical_card = ?";

    private Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    @Override
    public MedicalCard getByPatientId(int userId) throws MedicalCardDAOException {
        try {
            return getByIntParameter(userId, FIND_BY_PATIENT_ID);
        } catch (SQLException e) {
            throw new MedicalCardDAOException(e.getMessage());
        }
    }

    @Override
    public Entity getById(int id) throws MedicalCardDAOException {
        try {
            return getByIntParameter(id, FIND_BY_ID);
        } catch (SQLException e) {
            throw new MedicalCardDAOException(e.getMessage());
        }
    }

    private MedicalCard getByIntParameter(int parameter, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, parameter);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<MedicalCard> cards = unmarshal(resultSet);

        if (cards != null && !cards.isEmpty()) {
            return cards.get(0);
        }
        return null;
    }

    @Override
    public MedicalCard getByPatientFullName(String lastName, String firstName, String patronymic) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PATIENT_FULL_NAME);
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, patronymic);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<MedicalCard> cards = unmarshal(resultSet);
            if (cards != null && !cards.isEmpty()) {
                return  cards.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MedicalCard> getByPatientLastName(String lastName) {
        if (lastName != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PATIENT_LAST_NAME);
                preparedStatement.setString(1, lastName);

                ResultSet resultSet = preparedStatement.executeQuery();
                List<MedicalCard> cards = unmarshal(resultSet);

                return cards;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<MedicalCard> unmarshal(ResultSet resultSet) {

        if (resultSet != null) {
            try {
                List<MedicalCard> cardList = new ArrayList<>();

                while (resultSet.next()) {
                    MedicalCard card = new MedicalCard();

                    card.setUserID(resultSet.getInt("iduser"));
                    card.setSex(resultSet.getByte("sex"));
                    card.setDateOfBirth(resultSet.getString("birth_date"));
                    card.setId(resultSet.getInt("idmedical_card"));

                    cardList.add(card);
                }
                return cardList;
            } catch (SQLException e) {
                //TODO
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();

            return unmarshal(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();//TODO
        }
        return null;
    }

    @Override
    public boolean insert(Entity entity) throws MedicalCardDAOException {
        if (entity != null) {
            MedicalCard card = (MedicalCard) entity;

            try {
                if (connection == null){
                    logger.info("connection is null");
                }
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_MEDICAL_CARD);
                preparedStatement.setInt(1, card.getUserID());
                preparedStatement.setByte(2, card.getSex());
                preparedStatement.setString(3, card.getDateOfBirth());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                logger.error(entity.toString() + " wasn't inserted. Cause: " + e.getMessage());
                throw new MedicalCardDAOException("could not insert medical card");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Entity entity) {
        if (entity != null) {
            MedicalCard card = (MedicalCard) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MEDICAL_CARD);
                preparedStatement.setInt(1, card.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();//TODO
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Entity entity) {
        if (entity instanceof MedicalCard) {
            MedicalCard card = (MedicalCard) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MEDICAL_CARD);
                preparedStatement.setInt(3, card.getId());
                preparedStatement.setByte(1, card.getSex());
                preparedStatement.setString(2, card.getDateOfBirth());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();//TODO
            }
            return true;
        }
        return false;
    }


}
