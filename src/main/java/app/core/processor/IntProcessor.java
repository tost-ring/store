package app.core.processor;

import app.core.suite.Subject;
import app.core.suite.Suite;

@FunctionalInterface
public interface IntProcessor {

    default Subject ready() {
        return Suite.set();
    }
    void advance(int i)throws ProcessorException;
    default Subject finish()throws ProcessorException {
        return Suite.set();
    }
}
