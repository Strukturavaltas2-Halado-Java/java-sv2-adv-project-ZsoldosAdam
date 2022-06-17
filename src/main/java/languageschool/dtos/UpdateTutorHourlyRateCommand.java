package languageschool.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutorHourlyRateCommand {

    @Positive
    @Schema(description = "Hourly salary the tutor is paid for lessons in USD", example = "25.0")
    private double hourlyRate;
}
