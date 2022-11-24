package storage;

import entities.User;

import java.util.List;

public interface UserStorage {

    void addUser(User user);
    void removeUser(User user);
    void updateUser(User user);
    User findUser(Long id);
    List<User> listUser();

}
