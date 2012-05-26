/**
 * Альбомы 
 */
DROP TABLE fp_album;
CREATE TABLE fp_album (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Название */
	released DATE NOT NULL, 	/* Дата выхода */
	description VARCHAR2(1024) , 	/* Описание */
	group_id NUMBER(11,0) NOT NULL, 
    CONSTRAINT fp_album_pk PRIMARY KEY (id), 
	CONSTRAINT fp_album_group_fk FOREIGN KEY (group_id) REFERENCES fp_group(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_album_id_inc;

CREATE SEQUENCE fp_seq_album_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_album_id_inc 
BEFORE INSERT ON fp_album 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_album_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_album_id_inc; 
/
