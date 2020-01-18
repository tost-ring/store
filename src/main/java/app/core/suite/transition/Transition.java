package app.core.suite.transition;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Transition {

    static Subject needless(Subject state, Subject input) {
        return input;
    }

    static Subject lossy(Subject state, Subject input) {
        return Suite.set();
    }

    void revel(Subject state, Subject in);

    default Subject play() {
        revel(Suite.set(), Suite.set());
        return Suite.set();
    }

    default Subject play(Subject in) {
        revel(Suite.set(), in);
        return Suite.set();
    }

    default Subject play(Subject state, Subject in) {
        revel(state, in);
        return Suite.set();
    }

    default Subject gamble() throws Exception {
        revel(Suite.set(), Suite.set());
        return Suite.set();
    }

    default Subject gamble(Subject in) throws Exception {
        revel(Suite.set(), in);
        return Suite.set();
    }

    default Subject gamble(Subject state, Subject in) throws Exception {
        revel(state, in);
        return Suite.set();
    }
}
