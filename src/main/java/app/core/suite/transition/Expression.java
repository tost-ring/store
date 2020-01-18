package app.core.suite.transition;

import app.core.suite.Subject;

@FunctionalInterface
public interface Expression extends Statement {

    @Override
    default void revel() {
        play();
    }

    @Override
    Subject play();

    @Override
    default Subject play(Subject in) {
        return play();
    }

    @Override
    default Subject play(Subject state, Subject in) {
        return play();
    }

    @Override
    default Subject gamble() throws Exception {
        return play();
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        return play();
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        return play();
    }
}
