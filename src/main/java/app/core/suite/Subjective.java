package app.core.suite;

public interface Subjective {
    default Subject toSubject() {
        return Prospect.classpathSubjectively(Suite.set(this));
    }

    default Subject fromSubject(Subject subject) {
        return Prospect.classpathObjectively(Suite.set(Object.class, this).set(Subject.class, subject));
    }
}
