package by.epam.javatraining.webproject.model.dao.implementation;

import by.epam.javatraining.webproject.model.dao.AbstractDAO;
import by.epam.javatraining.webproject.model.dao.IUserDAO;
import by.epam.javatraining.webproject.model.entity.Entity;
import by.epam.javatraining.webproject.model.entity.role.UserRole;
import by.epam.javatraining.webproject.model.entity.User;
import by.epam.javatraining.webproject.model.dao.exception.UserDAOException;
import by.epam.javatraining.webproject.util.Fields;
import by.epam.javatraining.webproject.util.Parameters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO implements IUserDAO {

    private static final String FIND_ALL = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type, phone_number, blocked FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role";
    private static final String FIND_ALL_OF_TYPE = FIND_ALL + " WHERE user_role.role_type = ?";
    private static final String FIND_BY_ID = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type, phone_number, blocked FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role WHERE iduser = ?";

    private static final String INSERT_USER = "INSERT INTO user(first_name, last_name, patronymic, login, password, iduser_role, phone_number, blocked) VALUES(?, ?, ?, ?, ?, ?, ?, false)";
    private static final String GET_USER_ID = "SELECT iduser FROM hospital.user WHERE login = ? ";
    private static final String FIND_BY_LOGIN = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type, phone_number, blocked FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role WHERE login = ?";

    private static final String UPDATE_USER = "UPDATE user SET first_name=?, last_name=?, patronymic=?, login=?, password=?, iduser_role=?, phone_number=? WHERE iduser=?";
    private static final String DELETE_USER = "DELETE FROM user WHERE iduser = ?";
    private static final String BLOCK_USER = "UPDATE user SET blocked=? WHERE iduser=?";

    @Override
    public User getByLogin(String login) throws UserDAOException {

        if (login != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_LOGIN);
                preparedStatement.setString(1, login);
                logger.debug("login: " + login);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<User> userList = unmarshal(resultSet);
                User user = null;
                if (userList != null && !userList.isEmpty()) {
                    user = userList.get(0);
                }
                return user;
            } catch (SQLException e) {
                throw new UserDAOException("could not get user by id: " + e.getMessage());
            }
        } else {
            logger.info("login was null");
        }
        return null;
    }

    @Override
    public boolean setBlocking(int id, boolean blocked) throws UserDAOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(BLOCK_USER);
            preparedStatement.setBoolean(1, blocked);
            preparedStatement.setInt(2, id);

            logger.debug("blocked id - " + id + ", blocked - " + blocked);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new UserDAOException("could not block user with id " + id + ": " + e.getMessage());
        }
    }

    private List<User> unmarshal(ResultSet resultSet) throws SQLException {

        if (resultSet != null) {
            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt(Fields.IDUSER));
                user.setLogin(resultSet.getString(Parameters.LOGIN));
                user.setPassword(resultSet.getInt(Parameters.PASSWORD));
                user.setName(resultSet.getString(Fields.FIRST_NAME));
                user.setPhoneNumber(resultSet.getString(Parameters.PHONE_NUMBER));
                user.setSurname(resultSet.getString(Fields.LAST_NAME));
                user.setPatronymic(resultSet.getString(Parameters.PATRONYMIC));
                user.setRole(UserRole.valueOf(resultSet.getString(Fields.ROLE_TYPE)));
                user.setBlocked(resultSet.getBoolean(Fields.BLOCKED));
                userList.add(user);
            }
            return userList;
        }
        return null;
    }

    @Override
    public List<User> getAll() throws UserDAOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();

            return unmarshal(resultSet);
        } catch (SQLException e) {
            throw new UserDAOException("could not get users: " + e.getMessage());
        }
    }

    public List<User> getAllOfType(UserRole role) throws UserDAOException {
        try {
            if (role != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_OF_TYPE);
                preparedStatement.setString(1, role.name());
                ResultSet resultSet = preparedStatement.executeQuery();

                return unmarshal(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new UserDAOException("could not get users by type: " + e.getMessage());
        }
    }

    @Override
    public Entity getById(int id) throws UserDAOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = unmarshal(resultSet);

            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new UserDAOException("could not get user by id: " + e.getMessage());
        }
    }

    @Override
    public boolean insert(Entity entity) throws UserDAOException {
        if (entity instanceof User) {

            User user = (User) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
                preparedStatement.setInt(6, user.getRole().ordinal() + 1);
                preparedStatement.setString(4, user.getLogin());
                preparedStatement.setLong(5, user.getPassword());
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(7, user.getPhoneNumber());
                preparedStatement.setString(2, user.getSurname());
                preparedStatement.setString(3, user.getPatronymic());

                preparedStatement.execute();

            } catch (SQLException e) {
                throw new UserDAOException("could not insert user: " + e.getMessage());
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean delete(Entity entity) throws UserDAOException {
        if (entity instanceof User) {
            User user = (User) entity;
            deleteById(user.getId());
            return true;
        }
        return false;
    }

    public void deleteById(int id) throws UserDAOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new UserDAOException("could not delete user by id: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Entity entity) throws UserDAOException {

        if (entity instanceof User) {
            User user = (User) entity;

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
                preparedStatement.setInt(8, user.getId());
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getSurname());
                preparedStatement.setString(7, user.getPhoneNumber());
                preparedStatement.setString(3, user.getPatronymic());
                preparedStatement.setString(4, user.getLogin());
                preparedStatement.setLong(5, user.getPassword());
                preparedStatement.setInt(6, user.getRole().ordinal() + 1);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new UserDAOException("could not update user: " + e.getMessage());
            }
            return true;
        }
        return false;
    }
}
