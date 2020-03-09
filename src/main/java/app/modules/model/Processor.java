package app.modules.model;

import app.core.suite.Subject;
import app.core.suite.action.Action;

import java.util.function.Predicate;

public interface Processor<T>  {
    Subject getState();
    Node getStartNode();
    Node connect(Node from, Predicate<T> condition, Action action);
    void connect(Node from, Node to, Predicate<T> condition, Action action);
    void process(Iterable<T> iterable) throws ProcessorException;
}
