package languageschool.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TutorNotFoundException extends AbstractThrowableProblem {

    public TutorNotFoundException(long id) {
        super(URI.create("tutors/not-found"), "Not found", Status.NOT_FOUND,
                String.format("Tutor not found by ID %d", id));
    }
}
