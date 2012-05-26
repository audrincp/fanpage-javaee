/**
 * Исполнители 
 */
DROP TABLE fp_performer;
CREATE TABLE fp_performer (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Имя */
	surname VARCHAR2(256) , 	/* Фамилия */
	patronymic VARCHAR2(256) , 	/* Отчество */
	sex VARCHAR2(256) NOT NULL, 	/* Пол */
	born_date DATE , 	/* Дата рождения */
	born_place VARCHAR2(256) , 	/* Место рождения */
	country VARCHAR2(256) NOT NULL, 	/* Страна */
	death DATE , 	/* Дата смерти */
	description VARCHAR2(1024) , 	/* Описание */
    CONSTRAINT fp_performer_pk PRIMARY KEY (id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_performer_id_inc;

CREATE SEQUENCE fp_seq_performer_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_performer_id_inc 
BEFORE INSERT ON fp_performer 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_performer_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_performer_id_inc; 
/
