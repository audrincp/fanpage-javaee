/**
 * Новости 
 */
DROP TABLE fp_news;
CREATE TABLE fp_news (
    id NUMBER(11,0) NOT NULL,  /* id */
	title VARCHAR2(256) NOT NULL, 	/* Заголовок */
	news_date DATE NOT NULL, 	/* Дата */
	short VARCHAR2(1024) NOT NULL, 	/* Анонс */
	full VARCHAR2(1024) NOT NULL, 	/* Полный текст */
    CONSTRAINT fp_news_pk PRIMARY KEY (id) 
);
/*
 * автоинкремент id
 */
DROP SEQUENCE fp_seq_news_id_inc;

CREATE SEQUENCE fp_seq_news_id_inc INCREMENT BY 1 START WITH 1;

CREATE OR REPLACE TRIGGER fp_trg_news_id_inc 
BEFORE INSERT ON fp_news 
FOR EACH ROW 
BEGIN 
    SELECT fp_seq_news_id_inc.NEXTVAL INTO :new.id FROM DUAL;
END fp_trg_news_id_inc; 
/
