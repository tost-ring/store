package app.core.suite.action;

import app.core.suite.Subject;

@FunctionalInterface
public interface HazardousExpression extends Expression {

    @Override
    default Subject play() {
        try {
            return gamble();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject in) {
        try {
            return gamble();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default Subject play(Subject state, Subject in) {
        try {
            return gamble();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    Subject gamble() throws Exception;

    @Override
    default Subject gamble(Subject in) throws Exception {
        return gamble();
    }

    @Override
    default Subject gamble(Subject state, Subject in) throws Exception {
        return gamble();
    }
}
