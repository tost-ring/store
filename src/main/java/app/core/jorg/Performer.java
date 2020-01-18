package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.Subjective;
import app.core.suite.Suite;
import app.modules.graph.DupleGraph;
import app.modules.graph.HashDupleGraph;

public interface Performer {
    Subject subjectively(Object object);
    boolean objectively(Object object, Subject subject);
    Object construct(Class<?> type);
}
