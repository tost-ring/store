package app.modules.graph;

import java.util.Collection;

public interface Graph<N> {
    void putNode(N node);
    void removeNode(N node);
    Collection<N> getNodes();
    Collection<N> getNodes(N node);
    N getNode();
    N getNode(N node);
    int countNodes();
    int countNodes(N node);
    boolean containsNode(N node);
    void detach(N src, N dest);
    boolean linked(N src, N dest);
}
