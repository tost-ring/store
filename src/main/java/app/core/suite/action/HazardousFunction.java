package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface HazardousFunction extends HazardousImpression, Function {

    @Override
    default void revel(Subject state, Subject in) {
        try {
            gamble(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void revel(Subject in) {
        try {
            gamble(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void strive(Subject in) throws Exception {
        gamble(in);
    }

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
    default Subject play(Subject state, Subject in) {
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

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        return gamble(in);
    }
}
