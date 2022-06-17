package languageschool.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonTimeScheduleCommand {

    @NonNull
    @Schema(description = "Starting date/time of the lesson", example = "2020-05-20T10:00")
    private LocalDateTime start;

    @Positive
    @Schema(description = "Duration of the lesson in hours", example = "1.0")
    private double duration;
}
