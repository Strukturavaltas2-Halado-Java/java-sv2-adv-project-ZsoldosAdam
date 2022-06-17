package languageschool.dtos;

import languageschool.models.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {

    private Long id;
    private LessonTutorDto tutor;
    private Language language;
    private LocalDateTime start;
    private double duration;
}
