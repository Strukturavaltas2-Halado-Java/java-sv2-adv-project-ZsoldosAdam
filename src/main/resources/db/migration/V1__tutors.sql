CREATE TABLE tutors
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)          NULL,
    hourly_rate DOUBLE                NULL,
    CONSTRAINT pk_tutors PRIMARY KEY (id)
);

CREATE TABLE tutor_languages
(
    tutor_id BIGINT       NOT NULL,
    level    VARCHAR(255) NULL,
    language INT          NOT NULL,
    CONSTRAINT pk_tutor_languages PRIMARY KEY (tutor_id, language)
);

ALTER TABLE tutor_languages
    ADD CONSTRAINT fk_tutor_languages_on_tutor FOREIGN KEY (tutor_id) REFERENCES tutors (id);