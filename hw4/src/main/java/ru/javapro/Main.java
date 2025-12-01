package ru.javapro;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javapro.config.Config;
import ru.javapro.model.User;
import ru.javapro.service.UserService;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        UserService userService = context.getBean(UserService.class);

        int deleted = userService.deleteAllUsers();
        System.out.println("Удалено юзеров: " + deleted);

        User user1 = userService.createUser("user1");
        System.out.println("Создан юзер: " + user1);

        User user2 = userService.createUser("user2");
        System.out.println("Создан юзер: " + user2);

        User user3 = userService.createUser("user3");
        System.out.println("Создан юзер: " + user3);

        Optional<User> foundUser = userService.findUserById(user1.getId());
        foundUser.ifPresentOrElse(
                user -> System.out.println("Найден юзер по id " + user1.getId() + ": " + user),
                () -> System.out.println("Пользователь не найден")
        );

        List<User> allUsers = userService.findAllUsers();
        System.out.println("Всего пользователей: " + allUsers.size());
        allUsers.forEach(System.out::println);

        User updatedUser = userService.updateUser(user2.getId(), "user500000");
        System.out.println("Обновлён: " + updatedUser);

        userService.findUserById(user2.getId())
                .ifPresent(user -> System.out.println("После обновления: " + user));

        boolean isDeleted = userService.deleteUser(user3.getId());
        System.out.println("Юзер с id " + user3.getId() + " удалён: " + isDeleted);

        Optional<User> deletedUser = userService.findUserById(user3.getId());
        System.out.println("Пользователь существует: " + deletedUser.isPresent());

        List<User> finalUsers = userService.findAllUsers();
        System.out.println("Осталось пользователей: " + finalUsers.size());
        finalUsers.forEach(System.out::println);

        context.close();
    }
}