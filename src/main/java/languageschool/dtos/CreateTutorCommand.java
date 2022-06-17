package languageschool.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTutorCommand {

    @NotBlank
    private String name;

    @Positive
    private double hourlyRate;
}
