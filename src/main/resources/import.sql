INSERT INTO role (name_role) VALUES ('ROLE_ADMIN');
INSERT INTO role (name_role) VALUES ('ROLE_USER');

SET @adminRoleId = (SELECT id FROM role WHERE name_role = 'ROLE_ADMIN');
SET @userRoleId = (SELECT id FROM role WHERE name_role = 'ROLE_USER');
