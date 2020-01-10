package app.modules.graph;

public class GraphNode<D> {

    D data;

    public GraphNode() {
    }

    public GraphNode(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
