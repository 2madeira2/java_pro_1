package ru.javapro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javapro.dao.UserDao;
import ru.javapro.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String username) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        User user = new User(username);
        return userDao.save(user);
    }

    public Optional<User> findUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        return userDao.findById(id);
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public boolean deleteUser(Long id) {
        return userDao.deleteById(id);
    }

    public User updateUser(Long id, String newName) {
        if (userDao.findByUsername(newName).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        User user = new User(id, newName);
        return userDao.update(user);
    }

    public int deleteAllUsers() {
        return userDao.deleteAll();
    }
}
