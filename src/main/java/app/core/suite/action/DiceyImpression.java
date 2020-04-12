package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface DiceyImpression extends Action {

    void strive(Subject in) throws Exception;

    @Override
    default Subject play() {
        try {
            strive(Suite.set());
            return Suite.set();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject in) {
        try {
            strive(in);
            return Suite.set();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject gamble() throws Exception {
        strive(Suite.set());
        return Suite.set();
    }

    @Override
    default Subject gamble(Subject in) throws Exception {
        strive(in);
        return Suite.set();
    }
}
