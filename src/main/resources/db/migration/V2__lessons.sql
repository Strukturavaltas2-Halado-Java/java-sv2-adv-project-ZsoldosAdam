CREATE TABLE lessons
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    tutor_id BIGINT                NULL,
    language VARCHAR(255)          NULL,
    start    datetime              NULL,
    duration DOUBLE                NULL,
    CONSTRAINT pk_lessons PRIMARY KEY (id)
);

ALTER TABLE lessons
    ADD CONSTRAINT FK_LESSONS_ON_TUTOR FOREIGN KEY (tutor_id) REFERENCES tutors (id);