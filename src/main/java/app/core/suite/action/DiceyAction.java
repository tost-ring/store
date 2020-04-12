package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface DiceyAction extends Action {

    @Override
    default Subject play() {
        try {
            return gamble(Suite.set());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject in) {
        try {
            return gamble(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject gamble() throws Exception {
        return gamble(Suite.set());
    }

    @Override
    Subject gamble(Subject in) throws Exception;
}
