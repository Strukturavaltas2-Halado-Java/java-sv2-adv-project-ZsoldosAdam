package languageschool.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class IllegalMonthException extends AbstractThrowableProblem {

    public IllegalMonthException(int month) {
        super(URI.create("tutors/illegal-month"), "Illegal month argument", Status.BAD_REQUEST,
                String.format("Month %d is outside of legal bounds of 1..12", month));
    }
}
