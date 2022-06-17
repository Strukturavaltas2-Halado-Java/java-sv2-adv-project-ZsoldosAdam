package languageschool.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @Enumerated(EnumType.STRING)
    private Language language;

    private LocalDateTime start;

    private double duration;

    public LanguageLevel getLanguageLevel() {
        return tutor.getLanguageLevel(language);
    }
}
