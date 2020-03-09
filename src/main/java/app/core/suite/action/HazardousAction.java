package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface HazardousAction extends HazardousTransition, Transition {

    @Override
    default void revel(Subject state, Subject in) {
        try {
            gamble(state, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void strive(Subject state, Subject in) throws Exception {
        gamble(state, in);
    }

    @Override
    default Subject play() {
        try {
            return gamble(Suite.set(), Suite.set());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject in) {
        try {
            return gamble(Suite.set(), in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject state, Subject in) {
        try {
            return gamble(state, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject gamble() throws Exception {
        return gamble(Suite.set(), Suite.set());
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        return gamble(Suite.set(), in);
    }

    @Override
    Subject gamble(Subject state, Subject in) throws Exception;
}
