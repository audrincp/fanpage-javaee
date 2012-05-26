/**
 * Композиции 
 */
DROP TABLE fp_track;
CREATE TABLE fp_track (
    id NUMBER(11,0) NOT NULL,  				/* id */
	name VARCHAR2(256) NOT NULL, 			/* Название */
	length_seconds NUMBER(11,0) NOT NULL, 	/* Длительность (секунд) */
	label_id NUMBER(11,0) NOT NULL, 
	album_id NUMBER(11,0) NOT NULL, 
    CONSTRAINT fp_track_pk PRIMARY KEY (id), 
	CONSTRAINT fp_track_album_fk FOREIGN KEY (album_id) REFERENCES fp_album(id),
	CONSTRAINT fp_track_label_fk FOREIGN KEY (label_id) REFERENCES fp_label(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_track_id_inc;

CREATE SEQUENCE fp_seq_track_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_track_id_inc 
BEFORE INSERT ON fp_track 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_track_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_track_id_inc; 
/
