package app.modules.model.input;

import app.core.suite.Subject;
import app.core.suite.Subjective;

public interface Input extends Subjective {
    Subject valueFrom(Subject subject);
}
