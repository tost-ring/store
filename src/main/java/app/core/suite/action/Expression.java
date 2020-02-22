package app.core.suite.action;

import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.function.Supplier;

@FunctionalInterface
public interface Expression extends Statement {

    static Expression fromSupplier(Supplier<?> supplier) {
        return () -> Suite.set(supplier.get());
    }

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
