package languageschool.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import languageschool.models.Language;
import languageschool.models.LanguageLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTutorLanguagesCommand {

    @NonNull
    @Schema(description = "Language spoken by the tutor", example = "ENGLISH")
    private Language language;

    @NonNull
    @Schema(description = "The proficiency level at which the tutor speaks the language", example = "FLUENT")
    private LanguageLevel level;
}
