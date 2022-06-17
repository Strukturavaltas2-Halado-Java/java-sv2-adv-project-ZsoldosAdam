package languageschool.dtos;

import languageschool.models.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonCommand {

    private long tutorId;

    @NonNull
    private Language language;

    @NonNull
    private LocalDateTime start;

    @Positive
    private double duration;
}
