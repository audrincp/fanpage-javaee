/**
 * Клипы 
 */
DROP TABLE fp_video;
CREATE TABLE fp_video (
    id NUMBER(11,0) NOT NULL,  /* id */
	name VARCHAR2(256) NOT NULL, 	/* Название */
	href VARCHAR2(1024) NOT NULL, /* ссылка на youtube */
	length_seconds NUMBER(11,0) NOT NULL, 	/* Длительность (секунд) */
	description VARCHAR2(1024), 	/* Описание */
	track_id NUMBER(11,0) NOT NULL, 
    CONSTRAINT fp_video_pk PRIMARY KEY (id), 
	CONSTRAINT fp_video_track_fk FOREIGN KEY (track_id) REFERENCES fp_track(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_video_id_inc;

CREATE SEQUENCE fp_seq_video_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_video_id_inc 
BEFORE INSERT ON fp_video 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_video_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_video_id_inc; 
/
