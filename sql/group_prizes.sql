/**
 * Награды группы 
 */
DROP TABLE fp_group_prizes;
CREATE TABLE fp_group_prizes (
	id NUMBER(11,0) NOT NULL,
	group_id NUMBER(11,0) NOT NULL, 
	prize_id NUMBER(11,0) NOT NULL, 
	CONSTRAINT fp_group_prizes_pk PRIMARY KEY (id),
	CONSTRAINT fp_group_prizes_group_fk FOREIGN KEY (group_id) REFERENCES fp_group(id), 
	CONSTRAINT fp_group_prizes_prize_fk FOREIGN KEY (prize_id) REFERENCES fp_prize(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_group_prizes_id_inc;

CREATE SEQUENCE fp_seq_group_prizes_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_group_prizes_id_inc 
BEFORE INSERT ON fp_group_prizes 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_group_prizes_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_group_prizes_id_inc; 
/