package app.core.suite;

import app.core.jorg.StandardSolvent;

public interface Subjective {
    default Subject toSubject() {
        return StandardSolvent.classpathSubjectively(Suite.set(this));
    }

    default void fromSubject(Subject sub) {
        StandardSolvent.classpathObjectively(Suite.set(Object.class, this).set(Subject.class, sub));
    }
}
