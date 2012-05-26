/**
 * Награды 
 */
DROP TABLE fp_prize;
CREATE TABLE fp_prize (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Название */
	award_date DATE, 	/* Дата награждения */
	place VARCHAR2(256), 	/* Место награждения */
	description VARCHAR2(1024), 	/* Описание */
    CONSTRAINT fp_prize_pk PRIMARY KEY (id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_prize_id_inc;

CREATE SEQUENCE fp_seq_prize_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_prize_id_inc 
BEFORE INSERT ON fp_prize 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_prize_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_prize_id_inc; 
/
