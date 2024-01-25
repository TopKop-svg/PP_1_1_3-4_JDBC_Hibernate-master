package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Statement statement;

    public UserDaoJDBCImpl() {
        {
            try {
                statement = Util.getConnection().createStatement();
            } catch (SQLException e ) {
                throw new RuntimeException(e);
            }

        }
    }

    public void createUsersTable() {
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            statement.executeUpdate("CREATE TABLE users(id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) not null, lastname VARCHAR(50) not null, age INT(3) not null)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            String insertSQL = "INSERT INTO users(name, lastname, age) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(insertSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement pS = statement.getConnection()
                    .prepareStatement("DELETE FROM users WHERE id = (?)");
            pS.setLong(1, id);
            pS.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("name")
                        , resultSet.getString("lastname"), resultSet.getByte("age")));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void cleanUsersTable() {
        try {
            statement.executeUpdate("DELETE FROM users WHERE id > 0");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}