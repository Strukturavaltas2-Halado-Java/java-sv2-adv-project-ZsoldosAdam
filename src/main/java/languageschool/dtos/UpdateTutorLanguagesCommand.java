package languageschool.dtos;

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
    private Language language;

    @NonNull
    private LanguageLevel level;
}
