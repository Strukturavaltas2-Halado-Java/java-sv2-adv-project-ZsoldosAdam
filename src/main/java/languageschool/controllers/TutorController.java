package languageschool.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import languageschool.dtos.UpdateTutorLanguagesCommand;
import languageschool.dtos.CreateTutorCommand;
import languageschool.dtos.UpdateTutorHourlyRateCommand;
import languageschool.dtos.UpdateTutorNameCommand;
import languageschool.models.Language;
import languageschool.models.LanguageLevel;
import languageschool.dtos.TutorDto;
import languageschool.service.LanguageSchoolService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tutors")
@AllArgsConstructor
@Tag(name = "Operations with tutors")
public class TutorController {

    private final LanguageSchoolService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Searches tutors by name, language and language level")
    @ApiResponse(responseCode = "200", description = "Tutors sent")
    public List<TutorDto> find(@RequestParam Optional<String> name,
                                 @RequestParam Optional<Language> language,
                                 @RequestParam Optional<LanguageLevel> level) {
        return service.findTutors(name, language, level);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Finds tutor by ID")
    @ApiResponse(responseCode = "200", description = "Tutor sent")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public TutorDto findById(@PathVariable("id") long id) {
        return service.findTutorById(id);
    }

    @GetMapping("/{id}/salary/{month}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Finds tutor's salary on specified month by ID")
    @ApiResponse(responseCode = "200", description = "Tutor salary sent")
    @ApiResponse(responseCode = "400", description = "Illegal month argument")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public double findSalaryById(@PathVariable("id") long id, @PathVariable("month") int month) {
        return service.findTutorSalaryById(id, month);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates new tutor")
    @ApiResponse(responseCode = "201", description = "Tutor created")
    public TutorDto save(@Valid @RequestBody CreateTutorCommand command) {
        return service.saveTutor(command);
    }

    @PutMapping("/{id}/name")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Updates tutor's name by ID")
    @ApiResponse(responseCode = "202", description = "Name updated")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public TutorDto updateNameById(@PathVariable("id") long id,
                                   @Valid @RequestBody UpdateTutorNameCommand command) {
        return service.updateTutorNameById(id, command);
    }

    @PutMapping("/{id}/hourly-rate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Updates tutor's hourly rate by ID")
    @ApiResponse(responseCode = "202", description = "Hourly rate updated")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public TutorDto updateHourlyRateById(@PathVariable("id") long id,
                                         @Valid @RequestBody UpdateTutorHourlyRateCommand command) {
        return service.updateTutorHourlyRateById(id, command);
    }

    @PutMapping("/{id}/languages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Updates tutor's languages by ID")
    @ApiResponse(responseCode = "202", description = "Languages updated")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public TutorDto updateLanguagesById(@PathVariable("id") long id,
                                        @Valid @RequestBody UpdateTutorLanguagesCommand command) {
        return service.updateTutorLanguagesById(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes tutor by ID")
    @ApiResponse(responseCode = "204", description = "Tutor deleted")
    @ApiResponse(responseCode = "404", description = "Tutor not found")
    public void deleteById(@PathVariable("id") long id) {
        service.deleteTutorById(id);
    }
}
