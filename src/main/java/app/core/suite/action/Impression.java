package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface Impression extends Action {

    void revel(Subject in);

    @Override
    default Subject play() {
        revel(Suite.set());
        return Suite.set();
    }

    @Override
    default Subject play(Subject in) {
        revel(in);
        return Suite.set();
    }

    @Override
    default Subject gamble() throws Exception {
        revel(Suite.set());
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        revel(in);
        return Suite.set();
    }
}
