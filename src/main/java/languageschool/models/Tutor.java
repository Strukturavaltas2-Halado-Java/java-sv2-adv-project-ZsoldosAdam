package languageschool.models;

import languageschool.exceptions.IllegalMonthException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DateTimeException;
import java.time.Month;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tutors")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "tutor_languages", joinColumns = {@JoinColumn(name = "tutor_id")})
    @MapKeyColumn(name = "language")
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Map<Language, LanguageLevel> languages = new LinkedHashMap<>();

    @OneToMany(mappedBy = "tutor")
    private List<Lesson> lessons = new ArrayList<>();

    @Column(name = "hourly_rate")
    private double hourlyRate;

    public void putLanguage(Language language, LanguageLevel languageLevel) {
        if (languageLevel == LanguageLevel.NONE) {
            languages.remove(language);
        } else {
            languages.put(language, languageLevel);
        }
    }

    public LanguageLevel getLanguageLevel(Language language) {
        if (languages.containsKey(language)) {
            return languages.get(language);
        }
        return LanguageLevel.NONE;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setTutor(this);
    }

    public double getSalary(int month) {
        try {
            Month enumMonth = Month.of(month);
            double totalHours = lessons.stream()
                    .filter(l -> l.getStart().getMonth() == enumMonth)
                    .mapToDouble(Lesson::getDuration)
                    .sum();
            return totalHours * hourlyRate;
        } catch (DateTimeException e) {
            throw new IllegalMonthException(month);
        }
    }
}
