package languageschool.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "ID of the tutor assigned to the lesson", example = "123")
    private long tutorId;

    @NonNull
    @Schema(description = "The language being taught", example = "ENGLISH")
    private Language language;

    @NonNull
    @Schema(description = "Starting date/time of the lesson", example = "2020-05-20T10:00")
    private LocalDateTime start;

    @Positive
    @Schema(description = "Duration of the lesson in hours", example = "1.0")
    private double duration;
}
