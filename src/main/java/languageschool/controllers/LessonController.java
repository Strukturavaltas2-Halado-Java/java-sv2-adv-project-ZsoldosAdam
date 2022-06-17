package languageschool.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import languageschool.dtos.CreateLessonCommand;
import languageschool.dtos.LessonDto;
import languageschool.dtos.UpdateLessonTimeScheduleCommand;
import languageschool.models.*;
import languageschool.service.LanguageSchoolService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
@Tag(name = "Operations with lessons")
public class LessonController {

    private final LanguageSchoolService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Searches lessons by language and language level")
    @ApiResponse(responseCode = "200", description = "Lessons sent")
    public List<LessonDto> find(@RequestParam Optional<Language> language,
                                @RequestParam Optional<LanguageLevel> level) {
        return service.findLessons(language, level);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Finds lesson by ID")
    @ApiResponse(responseCode = "200", description = "Lesson sent")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public LessonDto findById(@PathVariable("id") long id) {
        return service.findLessonById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates new lesson")
    @ApiResponse(responseCode = "201", description = "Lesson created")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public LessonDto save(@Valid @RequestBody CreateLessonCommand command) {
        return service.saveLesson(command);
    }

    @PutMapping("/{id}/time-schedule")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Updates lesson's start time and duration by ID")
    @ApiResponse(responseCode = "202", description = "Time schedule updated")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public LessonDto updateTimeScheduleById(@PathVariable("id") long id,
                                            @Valid @RequestBody UpdateLessonTimeScheduleCommand command) {
        return service.updateLessonTimeScheduleById(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes lesson by ID")
    @ApiResponse(responseCode = "204", description = "Lesson deleted")
    @ApiResponse(responseCode = "404", description = "Lesson not found")
    public void deleteById(@PathVariable("id") long id) {
        service.deleteLessonById(id);
    }
}
