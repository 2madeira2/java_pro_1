package ru.javapro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javapro.repository.UserRepository;
import ru.javapro.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        User user = new User(username);
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public User updateUser(Long id, String newName) {
        if (userRepository.existsByUsername(newName)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));
        user.setUsername(newName);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteAllUsers() {
         userRepository.deleteAll();
    }
}
