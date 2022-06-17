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
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TutorControllerTest {

    @Autowired
    WebTestClient webClient;

    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    LessonRepository lessonRepository;

    long tutorId;

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
        long tutorId2 = Objects.requireNonNull(webClient.post().uri("/api/tutors")
                .bodyValue(new CreateTutorCommand("Jane Roe", 18.4))
                .exchange().returnResult(TutorDto.class).getResponseBody().blockFirst()).getId();
        webClient.put().uri("/api/tutors/{id}/languages", tutorId2)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.GERMAN, LanguageLevel.INTERMEDIATE))
                .exchange();
        webClient.put().uri("/api/tutors/{id}/languages", tutorId2)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.FRENCH, LanguageLevel.CONVERSATIONAL))
                .exchange();
    }

    @Test
    void testFindAll() {
        List<TutorDto> tutors = webClient.get().uri("/api/tutors").exchange()
                .expectStatus().isOk()
                .expectBodyList(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutors)
                .hasSize(2)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsOnly(
                        tuple("Jill Poe", 24.6,
                                Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE)),
                        tuple("Jane Roe", 18.4,
                                Map.of(Language.GERMAN, LanguageLevel.INTERMEDIATE, Language.FRENCH, LanguageLevel.CONVERSATIONAL))
                );
    }

    @Test
    void testFindByName() {
        List<TutorDto> tutors = webClient.get()
                .uri(b -> b.path("/api/tutors").queryParam("name", "PO").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutors)
                .hasSize(1)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsOnly(
                        tuple("Jill Poe", 24.6,
                                Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE))
                );
    }

    @Test
    void testFindByLanguage() {
        List<TutorDto> tutors = webClient.get()
                .uri(b -> b.path("/api/tutors").queryParam("language", "GERMAN").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutors)
                .hasSize(2)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsOnly(
                        tuple("Jill Poe", 24.6,
                                Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE)),
                        tuple("Jane Roe", 18.4,
                                Map.of(Language.GERMAN, LanguageLevel.INTERMEDIATE, Language.FRENCH, LanguageLevel.CONVERSATIONAL))
                );
    }

    @Test
    void testFindByLanguageAndLanguageLevel() {
        List<TutorDto> tutors = webClient.get()
                .uri(b -> b.path("/api/tutors").queryParam("language", "GERMAN")
                        .queryParam("level", "CONVERSATIONAL").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutors)
                .hasSize(1)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsOnly(
                        tuple("Jill Poe", 24.6,
                                Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE))
                );
    }

    @Test
    void testFindById() {
        TutorDto tutor = webClient.get().uri("/api/tutors/{id}", tutorId).exchange()
                .expectStatus().isOk()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jill Poe", 24.6,
                        Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE));
    }

    @Test
    void testDeleteAndFindById() {
        webClient.delete().uri("/api/tutors/{id}", tutorId).exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
        Problem problem = webClient.get().uri("/api/tutors/{id}", tutorId).exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class).returnResult().getResponseBody();
        assertThat(problem)
                .extracting(Problem::getType, Problem::getTitle, Problem::getStatus, Problem::getDetail)
                .containsExactly(URI.create("tutors/not-found"), "Not found", Status.NOT_FOUND,
                        String.format("Tutor not found by ID %d", tutorId));
    }

    @Test
    void testFindSalaryById() {
        webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.GERMAN,
                        LocalDateTime.parse("2022-05-02T12:00"), 0.8))
                .exchange();
        webClient.post().uri("/api/lessons")
                .bodyValue(new CreateLessonCommand(tutorId, Language.KOREAN,
                        LocalDateTime.parse("2022-05-03T11:30"), 0.7))
                .exchange();
        Double salary = webClient.get().uri("/api/tutors/{id}/salary/5", tutorId).exchange()
                .expectStatus().isOk()
                .expectBody(Double.class).returnResult().getResponseBody();
        assertThat(salary).isCloseTo(36.9, within(0.0005));
    }

    @Test
    void testSave() {
        TutorDto tutor = webClient.post().uri("/api/tutors")
                .bodyValue(new CreateTutorCommand("Jack Goe", 20.5))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages, TutorDto::getLessons)
                .containsExactly("Jack Goe", 20.5, Map.of(), List.of());
    }

    @Test
    void testUpdateNameById() {
        TutorDto tutor = webClient.put().uri("/api/tutors/{id}/name", tutorId)
                .bodyValue(new UpdateTutorNameCommand("Jack Goe"))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jack Goe", 24.6,
                        Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE));
    }

    @Test
    void testUpdateHourlyRateById() {
        TutorDto tutor = webClient.put().uri("/api/tutors/{id}/hourly-rate", tutorId)
                .bodyValue(new UpdateTutorHourlyRateCommand(28.0))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jill Poe", 28.0,
                        Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE));
    }

    @Test
    void testUpdateLanguageById() {
        TutorDto tutor = webClient.put().uri("/api/tutors/{id}/languages", tutorId)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.KOREAN, LanguageLevel.CONVERSATIONAL))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jill Poe", 24.6,
                        Map.of(Language.KOREAN, LanguageLevel.CONVERSATIONAL, Language.GERMAN, LanguageLevel.NATIVE));
    }

    @Test
    void testAddLanguageById() {
        TutorDto tutor = webClient.put().uri("/api/tutors/{id}/languages", tutorId)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.POLISH, LanguageLevel.BEGINNER))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jill Poe", 24.6,
                        Map.of(Language.KOREAN, LanguageLevel.FLUENT, Language.GERMAN, LanguageLevel.NATIVE, Language.POLISH, LanguageLevel.BEGINNER));
    }

    @Test
    void testRemoveLanguageById() {
        TutorDto tutor = webClient.put().uri("/api/tutors/{id}/languages", tutorId)
                .bodyValue(new UpdateTutorLanguagesCommand(Language.KOREAN, LanguageLevel.NONE))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TutorDto.class).returnResult().getResponseBody();
        assertThat(tutor)
                .extracting(TutorDto::getName, TutorDto::getHourlyRate, TutorDto::getLanguages)
                .containsExactly("Jill Poe", 24.6,
                        Map.of(Language.GERMAN, LanguageLevel.NATIVE));
    }
}
