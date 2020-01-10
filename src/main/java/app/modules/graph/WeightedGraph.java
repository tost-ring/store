package app.modules.graph;

public interface WeightedGraph<N, L> extends Graph<N> {
    void link(N quill, N pike, L link);
    L getLink(N quill, N pike);
}
