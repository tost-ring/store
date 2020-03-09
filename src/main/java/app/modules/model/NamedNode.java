package app.modules.model;

public class NamedNode extends Node {

    private String name;

    public NamedNode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
