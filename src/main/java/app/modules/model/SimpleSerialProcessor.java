package app.modules.model;

import app.core.suite.Graph;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.action.Action;

import java.util.function.Predicate;

public class SimpleSerialProcessor<T> implements Processor<T> {

    private Graph graph;
    private Subject state;
    private Node startNode;
    private Node currentNode;

    public SimpleSerialProcessor() {
        graph = new Graph();
        state = Suite.set();
        startNode = new Node();
        currentNode = startNode;
    }

    @Override
    public Subject getState() {
        return state;
    }

    public void setState(Subject state) {
        this.state = state;
    }

    @Override
    public Node getStartNode() {
        return startNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    @Override
    public Node connect(Node from, Predicate<T> condition, Action action) {
        Node to = new Node();
        connect(from, to, condition, action);
        return to;
    }

    @Override
    public void connect(Node from, Node to, Predicate<T> condition, Action action) {
        graph.connect(from, to, new ConditionalAction(condition, action));
    }

    public Node process(T input) throws ProcessorException{
        for(var s : graph.from(currentNode)) {
            ConditionalAction conditionalAction = s.get("via");
            if(conditionalAction.predicate.test(input)) {
                state = conditionalAction.action.play(state, Suite.set(input));
                currentNode = s.get("to");
                return currentNode;
            }
        }
        throw new ProcessorException("Unexcepted input " + input);
    }

    @Override
    public void process(Iterable<T> iterable) throws ProcessorException {
        for(T it : iterable) {
            process(it);
        }
    }

    class ConditionalAction {
        Predicate<T> predicate;
        Action action;

        public ConditionalAction(Predicate<T> predicate, Action action) {
            this.predicate = predicate;
            this.action = action;
        }
    }
}
