package by.epam.javatraining.webproject.dao;

import by.epam.javatraining.webproject.entity.Entity;
import by.epam.javatraining.webproject.entity.role.UserRole;
import by.epam.javatraining.webproject.entity.User;
import by.epam.javatraining.webproject.exception.UserDAOException;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO implements IUserDAO {

    private static Logger logger;

    {
        logger = Logger.getRootLogger();
    }

    public static final String FIND_ALL = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role";
    public static final String FIND_ALL_OF_TYPE = FIND_ALL + " WHERE user_role.role_type = ?";
    public static final String FIND_BY_ID = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role WHERE iduser = ?";

    public static final String INSERT_USER = "INSERT INTO user(first_name, last_name, patronymic, login, password, iduser_role) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String GET_USER_ID = "SELECT iduser FROM hospital.user WHERE login = ? ";
    public static final String FIND_BY_LOGIN = "SELECT iduser, first_name, last_name, login, password, patronymic, role_type FROM user INNER JOIN " +
            "user_role ON user.iduser_role = user_role.iduser_role WHERE login = ?";

    public static final String UPDATE_USER = "UPDATE user SET first_name=?, last_name=?, patronymic=?, login=?, password=?, iduser_role=? WHERE iduser=?";
    public static final String DELETE_USER = "DELETE FROM user WHERE iduser = ?";

    @Override
    public List<User> findByLastName(String lastName) {
        return null;
    }

    @Override
    public User findByFullName(String lastName, String firstName, String patronymic) {
        return null;
    }

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

    private List<User> unmarshal(ResultSet resultSet) throws SQLException {

        if (resultSet != null) {
                List<User> userList = new ArrayList<>();

                while (resultSet.next()) {
                    User user = new User();

                    user.setId(resultSet.getInt("iduser"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassword(resultSet.getLong("password"));
                    user.setName(resultSet.getString("first_name"));
                    user.setSurname(resultSet.getString("last_name"));
                    user.setPatronymic(resultSet.getString("patronymic"));
                    user.setRole(UserRole.valueOf(resultSet.getString("role_type")));
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
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_OF_TYPE);
            preparedStatement.setString(1, role.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            return unmarshal(resultSet);

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
                preparedStatement.setInt(7, user.getId());
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getSurname());
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
