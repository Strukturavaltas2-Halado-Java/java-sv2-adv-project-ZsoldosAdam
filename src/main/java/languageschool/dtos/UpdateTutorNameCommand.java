package languageschool.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutorNameCommand {

    @NotBlank
    @Schema(description = "Name of the tutor", example = "Jill Doe")
    private String name;
}
