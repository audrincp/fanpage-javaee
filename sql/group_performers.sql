/**
 * Состав группы 
 */
DROP TABLE fp_group_performers;
CREATE TABLE fp_group_performers (
	id NUMBER(11,0) NOT NULL,  /* id */
	period VARCHAR2(256) , 	/* Период участия */
	performer_id NUMBER(11,0) NOT NULL, 
	group_id NUMBER(11,0) NOT NULL, 
	CONSTRAINT fp_group_perf_pk PRIMARY KEY (id),
	CONSTRAINT fp_group_perf_performer_fk FOREIGN KEY (performer_id) REFERENCES fp_performer(id), 
	CONSTRAINT fp_group_perf_group_fk FOREIGN KEY (group_id) REFERENCES fp_group(id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_group_performers_id_inc;

CREATE SEQUENCE fp_seq_group_performers_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_group_performers_id_inc 
BEFORE INSERT ON fp_group_performers 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_group_performers_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_group_performers_id_inc; 
/
