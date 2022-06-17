package languageschool.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class LessonNotFoundException extends AbstractThrowableProblem {

    public LessonNotFoundException(long id) {
        super(URI.create("lessons/not-found"), "Not found", Status.NOT_FOUND,
                String.format("Lesson not found by ID %d", id));
    }
}
