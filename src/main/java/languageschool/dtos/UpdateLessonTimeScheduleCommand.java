package languageschool.dtos;

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
    private LocalDateTime start;

    @Positive
    private double duration;
}
