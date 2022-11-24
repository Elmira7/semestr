package storage;

import entities.User;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserStorageDataBase implements UserStorage {

    @Override
    public void addUser(User user) {

        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into users (login, name, password, email, phone_number, role, uuid) values (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getRole());
            statement.setString(7, user.getUuid());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeUser(User user) {
        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("delete from users where id = ?");
            statement.setLong(1, user.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("update users set login = ?, name = ?, password = ?, email = ?, phone_number = ?, role = ?, uuid = ? where id = ?");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getRole());
            statement.setString(7, user.getUuid());
            statement.setLong(8, user.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(Long id) {
        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from users where id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return User.builder()
                        .id(resultSet.getLong("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .name(resultSet.getString("name"))
                        .phoneNumber(resultSet.getString("phone_number"))
                        .role(resultSet.getString("role"))
                        .uuid(resultSet.getString("uuid"))
                        .build();
            } else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> listUser() {
        List<User> users = new ArrayList<>();
        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                users.add(User.builder()
                        .id(resultSet.getLong("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .email(resultSet.getString("email"))
                        .name(resultSet.getString("name"))
                        .phoneNumber(resultSet.getString("phone_number"))
                        .role(resultSet.getString("role"))
                        .uuid(resultSet.getString("uuid"))
                        .build());
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
