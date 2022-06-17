package languageschool.controllers;

import languageschool.dtos.*;
import languageschool.models.Language;
import languageschool.models.LanguageLevel;
import languageschool.repositories.LessonRepository;
import languageschool.repositories.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LessonControllerIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    LessonRepository lessonRepository;

    long tutorId;
    long lessonId;

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        tutorRepository.deleteAll();
        tutorId = Objects.requireNonNull(webClient.post().uri("/api/tutors")
                .bodyValue(new CreateTutorCommand("Jill Poe", 24.6))
                .exchange().returnResult(TutorDto.class).getResponseBody().blockFirst()).getId();
        webClient.put().uri("/api/tutors/{id}/languages", tutorId)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.KOREAN, LanguageLevel.FLUENT))
                .exchange();
        webClient.put().uri("/api/tutors/{id}/languages", tutorId)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.GERMAN, LanguageLevel.NATIVE))
                .exchange();
        lessonId = Objects.requireNonNull(webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.KOREAN,
                        LocalDateTime.parse("2022-05-01T10:00"), 1.0))
                .exchange().expectBody(LessonDto.class).returnResult().getResponseBody()).getId();
        webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.GERMAN,
                        LocalDateTime.parse("2022-05-02T12:00"), 0.4))
                .exchange();
        webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.KOREAN,
                        LocalDateTime.parse("2022-05-03T11:30"), 0.6))
                .exchange();
    }

    @Test
    void testFindAll() {
        List<LessonDto> lessons = webClient.get().uri("/api/lessons").exchange()
                .expectStatus().isOk()
                .expectBodyList(LessonDto.class).returnResult().getResponseBody();
        assertThat(lessons)
                .hasSize(3)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsOnly(
                        tuple("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-01T10:00"), 1.0),
                        tuple("Jill Poe", Language.GERMAN, LocalDateTime.parse("2022-05-02T12:00"), 0.4),
                        tuple("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-03T11:30"), 0.6)
                );
    }

    @Test
    void testFindByLanguage() {
        List<LessonDto> lessons = webClient.get()
                .uri(b -> b.path("/api/lessons").queryParam("language", "KOREAN").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LessonDto.class).returnResult().getResponseBody();
        assertThat(lessons)
                .hasSize(2)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsOnly(
                        tuple("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-01T10:00"), 1.0),
                        tuple("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-03T11:30"), 0.6)
                );
    }

    @Test
    void testFindByLanguageLevel() {
        List<LessonDto> lessons = webClient.get()
                .uri(b -> b.path("/api/lessons").queryParam("level", "NATIVE").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LessonDto.class).returnResult().getResponseBody();
        assertThat(lessons)
                .hasSize(1)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsOnly(
                        tuple("Jill Poe", Language.GERMAN, LocalDateTime.parse("2022-05-02T12:00"), 0.4)
                );
    }

    @Test
    void testFindByLanguageAndLanguageLevel() {
        List<LessonDto> lessons = webClient.get()
                .uri(b -> b.path("/api/lessons").queryParam("language", "GERMAN")
                        .queryParam("level", "FLUENT").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LessonDto.class).returnResult().getResponseBody();
        assertThat(lessons)
                .hasSize(1)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsOnly(
                        tuple("Jill Poe", Language.GERMAN, LocalDateTime.parse("2022-05-02T12:00"), 0.4)
                );
    }

    @Test
    void testFindNoneByLanguageAndLanguageLevel() {
        List<LessonDto> lessons = webClient.get()
                .uri(b -> b.path("/api/lessons").queryParam("language", "KOREAN")
                        .queryParam("level", "NATIVE").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LessonDto.class).returnResult().getResponseBody();
        assertThat(lessons).isEmpty();
    }

    @Test
    void testFindById() {
        LessonDto lesson = webClient.get().uri("/api/lessons/{id}", lessonId).exchange()
                .expectStatus().isOk()
                .expectBody(LessonDto.class).returnResult().getResponseBody();
        assertThat(lesson)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsExactly("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-01T10:00"), 1.0);
    }

    @Test
    void testDeleteAndFindById() {
        webClient.delete().uri("/api/lessons/{id}", lessonId).exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
        Problem problem = webClient.get().uri("/api/lessons/{id}", lessonId).exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();
        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(URI.create("lessons/not-found"), "Not found", Status.NOT_FOUND,
                        String.format("Lesson not found by ID %d", lessonId));
    }

    @Test
    void testDeleteTutorAndFindById() {
        webClient.delete().uri("/api/tutors/{id}", tutorId).exchange();
        Problem problem = webClient.get().uri("/api/lessons/{id}", lessonId).exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();
        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(URI.create("lessons/not-found"), "Not found", Status.NOT_FOUND,
                        String.format("Lesson not found by ID %d", lessonId));
    }

    @Test
    void testSave() {
        LessonDto lesson = webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.CHINESE,
                        LocalDateTime.parse("2022-05-04T16:00"), 1.2))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LessonDto.class).returnResult().getResponseBody();
        assertThat(lesson)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsExactly("Jill Poe", Language.CHINESE, LocalDateTime.parse("2022-05-04T16:00"), 1.2);
    }

    @Test
    void testUpdateTimeScheduleById() {
        LessonDto lesson = webClient.put().uri("/api/lessons/{id}/time-schedule", lessonId)
                .bodyValue(new UpdateLessonTimeScheduleCommand(LocalDateTime.parse("2022-05-05T16:30"), 0.8))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(LessonDto.class).returnResult().getResponseBody();
        assertThat(lesson)
                .extracting(l -> l.getTutor().getName(), LessonDto::getLanguage, LessonDto::getStart, LessonDto::getDuration)
                .containsExactly("Jill Poe", Language.KOREAN, LocalDateTime.parse("2022-05-05T16:30"), 0.8);
    }
}
