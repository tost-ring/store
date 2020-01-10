/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.Collection;

public class DirectedHashGraph<N> implements DirectedGraph<N> {
    private CompleteLexicon<N,N> lexicon;

    public DirectedHashGraph(){
        lexicon = new CompleteHashLexicon<>();
    }

    public DirectedHashGraph(WeightlessGraph<N> that){
        this();
        if(that != null) {
            for (N it : that.getNodes()) {
                for(N itt : that.getNodes(it)){
                    link(it,itt);
                }
            }
        }
    }

    @Override
    public Collection<N> getUpstreamNodes(N node) {
        return lexicon.getBeings(node);
    }

    @Override
    public N getUpstreamNode(N node) {
        return lexicon.getBeing(node);
    }

    @Override
    public int countUpstreamNodes(N node) {
        return lexicon.countBeings(node);
    }

    @Override
    public void putNode(N node) {
        lexicon.putBeing(node);
    }

    @Override
    public void removeNode(N node) {
        lexicon.removeBeing(node);
    }

    @Override
    public Collection<N> getNodes() {
        return lexicon.getBeings();
    }

    @Override
    public Collection<N> getNodes(N node) {
        return lexicon.getTraits(node);
    }

    @Override
    public N getNode() {
        return lexicon.getTrait();
    }

    @Override
    public N getNode(N node) {
        return lexicon.getTrait(node);
    }

    @Override
    public int countNodes() {
        return lexicon.countBeings();
    }

    @Override
    public int countNodes(N node) {
        return lexicon.countTraits(node);
    }

    @Override
    public boolean containsNode(N node) {
        return lexicon.containsBeing(node);
    }

    @Override
    public void link(N src, N dest) {
        src = lexicon.putBeing(src);
        dest = lexicon.putTrait(dest);
        lexicon.grantTrait(src,dest);
    }

    @Override
    public void detach(N src, N dest) {
        lexicon.loseTrait(src,dest);
    }

    @Override
    public boolean linked(N src, N dest) {
        return lexicon.hasTrait(src,dest);
    }

    @Override
    public void merge(Graph<N> that) {
        for(N it : that.getNodes()){
            for(N itt : that.getNodes(it)){
                link(it,itt);
            }
        }
    }
}
