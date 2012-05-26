/**
 * Композиции на концерте 
 */
DROP TABLE fp_concert_tracks;
CREATE TABLE fp_concert_tracks (
	id NUMBER(11,0) NOT NULL,
	concert_id NUMBER(11,0) NOT NULL, 
	track_id NUMBER(11,0) NOT NULL, 
	CONSTRAINT fp_concert_tracks_pk PRIMARY KEY (id), 
	CONSTRAINT fp_concert_tracks_concert_fk FOREIGN KEY (concert_id) REFERENCES fp_concert(id), 
	CONSTRAINT fp_concert_tracks_track_fk FOREIGN KEY (track_id) REFERENCES fp_track(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_concert_tracks_id_inc;

CREATE SEQUENCE fp_seq_concert_tracks_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_concert_tracks_id_inc 
BEFORE INSERT ON fp_concert_tracks 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_concert_tracks_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_concert_tracks_id_inc; 
/
