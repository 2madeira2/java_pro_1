Задание 4 дедлайн 5.11
- Разверните локально postgresql БД, создайте таблицу users (id bigserial primary key, username varchar(255) unique);

- Создайте Maven проект и подключите к нему: драйвер postgresql, hickaricp, spring context.

- Создайте пул соединений в виде Spring бина

- Создайте класс User (Long id, String username)

- Реализуйте в виде бина класс UserDao который позволяет выполнять CRUD операции над пользователями

- Реализуйте в виде бина UserService, который позволяет: создавать, удалять, получать одного, получать всех пользователей из базы данных

- Создайте Spring Context, получите из него бин UserService и выполните все возможные операции