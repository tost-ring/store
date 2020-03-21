package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface IntProcessor {

    enum ResultType {
        RESULT, UNSUPPORTED, RESULT_READY
    }

    default Subject ready() {
        return Suite.set();
    }
    Subject advance(int i)throws ProcessorException;
    default Subject finish()throws ProcessorException {
        return Suite.set();
    }
}
