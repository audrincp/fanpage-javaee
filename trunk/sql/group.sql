/**
 * Группы 
 */
DROP TABLE fp_group;
CREATE TABLE fp_group (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Название */
	genre VARCHAR2(256) , 	/* Жанр */
	creation_date DATE , 	/* Дата создания */
	decay_date DATE , 	/* Дата распада */
	description VARCHAR2(1024) , 	/* Описание */
    CONSTRAINT fp_group_pk PRIMARY KEY (id)
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_group_id_inc;

CREATE SEQUENCE fp_seq_group_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_group_id_inc 
BEFORE INSERT ON fp_group 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_group_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_group_id_inc; 
/
