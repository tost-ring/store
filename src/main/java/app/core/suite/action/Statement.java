package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Statement extends Action {

    void revel();

    default Subject play() {
        revel();
        return Suite.set();
    }

    default Subject play(Subject in) {
        revel();
        return Suite.set();
    }

    default Subject gamble() {
        revel();
        return Suite.set();
    }

    default Subject gamble(Subject in) {
        revel();
        return Suite.set();
    }
}
