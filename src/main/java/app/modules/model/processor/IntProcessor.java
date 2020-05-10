package app.modules.model.processor;

import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.processor.ProcessorException;

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
