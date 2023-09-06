INSERT INTO tb_user (name, email, password) VALUES ('nina', 'nina@gmail.com', '1234');
INSERT INTO tb_user (name, email, password) VALUES ('leia', 'leia@gmail.com', '1234');

INSERT INTO tb_role (role_name) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (role_name) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);