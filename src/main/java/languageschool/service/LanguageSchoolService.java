package languageschool.service;

import languageschool.dtos.*;
import languageschool.exceptions.LessonNotFoundException;
import languageschool.exceptions.TutorNotFoundException;
import languageschool.models.*;
import languageschool.repositories.LessonRepository;
import languageschool.repositories.TutorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageSchoolService {

    private final TutorRepository tutorRepository;
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    public List<TutorDto> findTutors(Optional<String> name, Optional<Language> language, Optional<LanguageLevel> level) {
        return tutorRepository.findAll().stream()
                .filter(t -> filterTutors(t, name, language, level))
                .map(t -> modelMapper.map(t, TutorDto.class))
                .toList();
    }

    public TutorDto findTutorById(long id) {
        return modelMapper.map(getTutorById(id), TutorDto.class);
    }

    public double findTutorSalaryById(long id, int year, int month) {
        return getTutorById(id).getSalary(year, month);
    }

    @Transactional
    public TutorDto saveTutor(CreateTutorCommand command) {
        Tutor tutor = modelMapper.map(command, Tutor.class);
        tutorRepository.save(tutor);
        return modelMapper.map(tutor, TutorDto.class);
    }

    @Transactional
    public TutorDto updateTutorNameById(long id, UpdateTutorNameCommand command) {
        Tutor tutor = getTutorById(id);
        tutor.setName(command.getName());
        return modelMapper.map(tutor, TutorDto.class);
    }

    @Transactional
    public TutorDto updateTutorHourlyRateById(long id, UpdateTutorHourlyRateCommand command) {
        Tutor tutor = getTutorById(id);
        tutor.setHourlyRate(command.getHourlyRate());
        return modelMapper.map(tutor, TutorDto.class);
    }

    @Transactional
    public TutorDto updateTutorLanguagesById(long id, UpdateTutorLanguagesCommand command) {
        Tutor tutor = getTutorById(id);
        tutor.putLanguage(command.getLanguage(), command.getLevel());
        return modelMapper.map(tutor, TutorDto.class);
    }

    @Transactional
    public void deleteTutorById(long id) {
        try {
            lessonRepository.deleteAll(getTutorById(id).getLessons());
            tutorRepository.deleteById(id);
        } catch (TutorNotFoundException e) {
            // Tutor not found, no action necessary
        }
    }

    public List<LessonDto> findLessons(Optional<Language> language, Optional<LanguageLevel> level) {
        return lessonRepository.findAll().stream()
                .filter(l -> filterLessons(l, language, level))
                .map(l -> modelMapper.map(l, LessonDto.class))
                .toList();
    }

    public LessonDto findLessonById(long id) {
        return modelMapper.map(getLessonById(id), LessonDto.class);
    }

    @Transactional
    public LessonDto saveLesson(CreateLessonCommand command) {
        Tutor tutor = getTutorById(command.getTutorId());
        Lesson lesson = modelMapper.map(command, Lesson.class);
        tutor.addLesson(lesson);
        lessonRepository.save(lesson);
        return modelMapper.map(lesson, LessonDto.class);
    }

    @Transactional
    public LessonDto updateLessonTimeScheduleById(long id, UpdateLessonTimeScheduleCommand command) {
        Lesson lesson = getLessonById(id);
        lesson.setStart(command.getStart());
        lesson.setDuration(command.getDuration());
        return modelMapper.map(lesson, LessonDto.class);
    }

    @Transactional
    public void deleteLessonById(long id) {
        lessonRepository.deleteById(id);
    }

    private Tutor getTutorById(long id) {
        return tutorRepository.findById(id).orElseThrow(() -> new TutorNotFoundException(id));
    }

    private Lesson getLessonById(long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException(id));
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean filterTutors(Tutor tutor, Optional<String> name, Optional<Language> language, Optional<LanguageLevel> level) {
        if (name.isPresent() && !tutor.getName().toUpperCase().contains(name.get().toUpperCase())) return false;
        if (language.isPresent()) {
            int minLevel = level.map(LanguageLevel::getLevel).orElse(LanguageLevel.BEGINNER.getLevel());
            if (tutor.getLanguageLevel(language.get()).getLevel() < minLevel) return false;
        }
        return true;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean filterLessons(Lesson lesson, Optional<Language> language, Optional<LanguageLevel> level) {
        if (language.isPresent() && lesson.getLanguage() != language.get()) return false;
        if (level.isPresent() && lesson.getLanguageLevel().getLevel() < level.get().getLevel()) return false;
        return true;
    }
}
