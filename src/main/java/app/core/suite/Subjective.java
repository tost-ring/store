package app.core.suite;

public interface Subjective {
    default Subject toSubject() {
        return Prospect.classpathSubjectively(Suite.set(this));
    }

    default void fromSubject(Subject sub) {
        Prospect.classpathObjectively(Suite.set(Object.class, this).set(Subject.class, sub));
    }
}
