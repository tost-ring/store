package app.core.suite.transition;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface HazardousTransition extends Transition {

    @Override
    default void revel(Subject state, Subject in) {
        try {
            strive(state, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void strive(Subject state, Subject in) throws Exception;

    @Override
    default Subject play() {
        try {
            strive(Suite.set(), Suite.set());
            return Suite.set();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject in) {
        try {
            strive(Suite.set(), in);
            return Suite.set();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject state, Subject in) {
        try {
            strive(state, in);
            return Suite.set();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject gamble() throws Exception {
        strive(Suite.set(), Suite.set());
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        strive(Suite.set(), in);
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        strive(state, in);
        return Suite.set();
    }
}
