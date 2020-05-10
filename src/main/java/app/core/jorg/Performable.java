package app.core.jorg;

import app.core.suite.Subject;

public interface Performable {
    default Subject perform() {
        return StandardPerformer.perform(this);
    }
}
