package app.modules.graph;

public interface WeightlessGraph<N> extends Graph<N>{
    void link(N src, N dest);
    void merge(Graph<N> that);
}
