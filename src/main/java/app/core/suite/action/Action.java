package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Action {

    default Subject play() {
        return play(Suite.set());
    }

    Subject play(Subject in);

    default Subject gamble() throws Exception {
        return play(Suite.set());
    }

    default Subject gamble(Subject in) throws Exception {
        return play(in);
    }

}
