INSERT INTO role (name_role) VALUES ('ROLE_ADMIN');
INSERT INTO role (name_role) VALUES ('ROLE_USER');

# SET @adminRoleId = (SELECT id FROM role WHERE name_role = 'ROLE_ADMIN');
# SET @userRoleId = (SELECT id FROM role WHERE name_role = 'ROLE_USER');
#
# INSERT INTO person (user_name, age, email, password) VALUES ('admin', 35, 'admin@mail.ru', 'admin');
#
# INSERT INTO person_role (person_id, role_id) VALUES ((SELECT id FROM person WHERE user_name = 'admin'), @adminRoleId);