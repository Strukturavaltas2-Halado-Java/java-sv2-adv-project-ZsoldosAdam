package languageschool.dtos;

import languageschool.models.Language;
import languageschool.models.LanguageLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonTutorDto {

    private Long id;
    private String name;
    private Map<Language, LanguageLevel> languages;
    private double hourlyRate;
}
