INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'admin', 'admin', '2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'EMPLOYEE')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user1', 'admin', '2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'MANAGER')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'EMPLOYEE')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Max', 'Mustermann', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'user2', 'admin', '2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('user2', 'EMPLOYEE')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$UEIwGPJpM6Kfdk3.c6RLDOTtpDfXymwkqAL5LpiRZgizuShpwlq7u', 'elvis', 'elvis', '2016-01-01 00:00:00')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN')
INSERT INTO USERX_USERX_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'EMPLOYEE')