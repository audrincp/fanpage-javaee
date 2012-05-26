/**
 * Лейблы 
 */
DROP TABLE fp_label;
CREATE TABLE fp_label (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Название */
	country VARCHAR2(256), 	/* Страна */
	place VARCHAR2(256), 	/* Местоположение */
	description VARCHAR2(1024), 	/* Описание */
    CONSTRAINT fp_label_pk PRIMARY KEY(id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_label_id_inc;

CREATE SEQUENCE fp_seq_label_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_label_id_inc 
BEFORE INSERT ON fp_label 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_label_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_label_id_inc; 
/
