package app.core.suite.transition;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Action extends Transition {

    @Override
    default void revel(Subject state, Subject in) {
        play(state, in);
    }

    @Override
    default Subject play(Subject in) {
        return play(Suite.set(), in);
    }

    @Override
    default Subject play() {
        return play(Suite.set(), Suite.set());
    }

    @Override
    Subject play(Subject state, Subject in);

    @Override
    default Subject gamble() throws Exception {
        return play(Suite.set(), Suite.set());
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        return play(Suite.set(), in);
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        return play(state, in);
    }
}
