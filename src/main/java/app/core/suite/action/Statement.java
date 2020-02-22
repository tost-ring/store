package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Statement extends Transition {

    void revel();

    @Override
    default void revel(Subject state, Subject in) {
        revel();
    }

    @Override
    default Subject play() {
        revel();
        return Suite.set();
    }

    @Override
    default Subject play(Subject in) {
        revel();
        return Suite.set();
    }

    @Override
    default Subject play(Subject state, Subject in) {
        revel();
        return Suite.set();
    }

    @Override
    default Subject gamble() throws Exception {
        revel();
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        revel();
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        revel();
        return Suite.set();
    }
}
