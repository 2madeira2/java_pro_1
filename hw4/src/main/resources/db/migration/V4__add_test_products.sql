INSERT INTO products (account_number, balance, type, user_id)
VALUES ('4081781000000001', 15000, 'ACCOUNT', (SELECT id from users where username = 'user_test_1')),
       ('5500000000000001', 500, 'CARD', (SELECT id from users where username = 'user_test_2')),
       ('40817810000000001', 15000, 'ACCOUNT', (SELECT id from users where username = 'user_test_2')),
       ('4276123456789012', 5000, 'CARD', (SELECT id from users where username = 'user_test_3')),
       ('40817810000000002', 25000, 'ACCOUNT', (SELECT id from users where username = 'user_test_4')),
       ('4276123456789013', 10000, 'CARD', (SELECT id from users where username = 'user_test_4')),
       ('40817810000000003', 100000, 'ACCOUNT', (SELECT id from users where username = 'user_test_4')),
       ('4276123456789014', 7500, 'CARD', (SELECT id from users where username = 'user_test_5'));