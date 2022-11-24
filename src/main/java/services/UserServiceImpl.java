package services;

import entities.User;
import storage.UserStorage;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public void addUser(User user) {
        userStorage.addUser(user);
    }

    @Override
    public void removeUser(User user) {
        userStorage.removeUser(user);
    }

    @Override
    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    @Override
    public User findUser(Long id) {
        return userStorage.findUser(id);
    }

    @Override
    public List<User> listUser() {
        return userStorage.listUser();
    }

    @Override
    public User findUserByLogin(String login){
        for(User user: listUser()){
            if(user.getLogin() != null && user.getLogin().equals(login)){
                return user;
            }
        }
        return null;
    }

    @Override
    public User findUserByUUID(String uuid) {
        for(User user: listUser()){
            if(user.getUuid().equals(uuid)){
                return user;
            }
        }
        return null;
    }
}
