/**
 * Концерты 
 */
DROP TABLE fp_concert;
CREATE TABLE fp_concert (
    id NUMBER(11,0) NOT NULL,  /* id */
	country VARCHAR2(256) NOT NULL, 	/* Страна */
	place VARCHAR2(256) NOT NULL, 	/* Место проведения */
	concert_date DATE, 	/* Дата проведения */
	description VARCHAR2(1024), 	/* Описание */
    CONSTRAINT fp_concert_pk PRIMARY KEY (id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_concert_id_inc;

CREATE SEQUENCE fp_seq_concert_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_concert_id_inc 
BEFORE INSERT ON fp_concert 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_concert_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_concert_id_inc; 
/
