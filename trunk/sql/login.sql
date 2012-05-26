/**
 * Модераторы 
 */
DROP TABLE fp_login;
CREATE TABLE fp_login (
    id NUMBER(11,0) NOT NULL,  /* id */
	login VARCHAR2(256) NOT NULL, 	/* Логин */
	password VARCHAR2(256) NOT NULL, 	/* Пароль */
	is_admin NUMBER(11,0) NOT NULL, 	/* Права */
    CONSTRAINT fp_login_pk PRIMARY KEY (id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_login_id_inc;

CREATE SEQUENCE fp_seq_login_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_login_id_inc 
BEFORE INSERT ON fp_login 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_login_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_login_id_inc; 
/
