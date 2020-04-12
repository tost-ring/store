package app.core.jorg;

import app.core.suite.Graph;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.Objects;

public class Xkey {
    private Object object;
    private Object label;
    private Subject direct;
    private Graph graph;

    public Xkey(Object object, Object label) {
        this.object = object;
        this.label = label;
        direct = Suite.set();
        graph = new Graph();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getLabel() {
        return label;
    }

    public void setLabel(Object label) {
        this.label = label;
    }

    public Subject getDirect() {
        return direct;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Xkey && Objects.equals(object, ((Xkey) o).object) && Objects.equals(label, ((Xkey) o).label);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(object) ^ Objects.hashCode(label);
    }

    @Override
    public String toString() {
        return label != null ? label.toString() : object.toString();
    }

    public String dataAsString() {
        return " [ " + label + " ] " + direct.values().toString(" ] ") + "\n" + graph.toString();
    }
}
