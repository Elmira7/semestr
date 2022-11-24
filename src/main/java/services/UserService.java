package services;

import entities.User;

import java.util.List;

public interface UserService {

    void addUser(User user);
    void removeUser(User user);
    void updateUser(User user);
    User findUser(Long id);
    List<User> listUser();

    User findUserByLogin(String login);
    User findUserByUUID(String uuid);
}
