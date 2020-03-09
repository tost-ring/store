package app.modules.model;

import app.core.suite.Graph;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public class TextSerialProcessor {

    private Graph graph;
    private Node startNode;
    private Node endNode;
    private Node currentNode;

    public TextSerialProcessor() {
        graph = new Graph();
        startNode = new Node();
        endNode = new Node();
        currentNode = startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public Node connect(Node from, IntPredicate condition, IntConsumer consumer) {
        Node to = new Node();
        connect(from, condition, consumer, to);
        return to;
    }

    public void connect(Node from, IntPredicate condition, IntConsumer consumer, Node to) {
        graph.connect(from, to, new ConditionalConsumption(condition, consumer));
    }

    private Node process(int character) throws ProcessorException{
        for(var s : graph.from(currentNode)) {
            ConditionalConsumption conditionalConsumption = s.get("via");
            if(conditionalConsumption.predicate.test(character)) {
                conditionalConsumption.consumer.accept(character);
                setCurrentNode(s.get("to"));
                return currentNode;
            }
        }
        throw new ProcessorException("Unexcepted input " + Character.toString(character));
    }

    public void process(String string) throws ProcessorException {
        start();
        partProcess(string);
        end();
    }

    public void partProcess(String string) throws ProcessorException {
        for (PrimitiveIterator.OfInt iter = string.chars().iterator(); iter.hasNext(); ) {
            int it = iter.next();
            process(it);
        }
    }

    public void start() {
        setCurrentNode(getStartNode());
    }

    public void end() throws ProcessorException{

        ConditionalConsumption conditionalConsumption = graph.getLinks(currentNode, endNode).god(null);
        if(conditionalConsumption == null) {
            throw new ProcessorException("Unexcepted end of data");
        }
        conditionalConsumption.consumer.accept(0);
        currentNode = endNode;
    }

    static class ConditionalConsumption {
        IntPredicate predicate;
        IntConsumer consumer;

        public ConditionalConsumption(IntPredicate predicate, IntConsumer consumer) {
            this.predicate = predicate;
            this.consumer = consumer;
        }
    }
}
