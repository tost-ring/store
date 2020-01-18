package app.core.suite.transition;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Function extends Impression {

    @Override
    default void revel(Subject in) {
        play(in);
    }

    @Override
    default Subject play() {
        return play(Suite.set());
    }

    @Override
    Subject play(Subject in);

    @Override
    default Subject play(Subject state, Subject in) {
        return play(in);
    }

    @Override
    default Subject gamble() throws Exception {
        return play(Suite.set());
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        return play(in);
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        return play(in);
    }
}
