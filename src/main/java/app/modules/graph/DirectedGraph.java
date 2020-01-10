package app.modules.graph;

import java.util.Collection;

public interface DirectedGraph<N> extends WeightlessGraph<N> {
    Collection<N> getUpstreamNodes(N node);
    N getUpstreamNode(N node);
    int countUpstreamNodes(N node);
}
