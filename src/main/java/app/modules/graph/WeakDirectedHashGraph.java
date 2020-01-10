/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WeakDirectedHashGraph<N> implements DirectedGraph<N> {
    private Lexicon<N,N> lexicon;

    public WeakDirectedHashGraph(){
        lexicon = new HashLexicon<>();
    }

    public WeakDirectedHashGraph(WeightlessGraph<N> that){
        this();
        if(that != null) {
            for (N it : that.getNodes()) {
                for(N itt : that.getNodes(it)){
                    link(it,itt);
                }
            }
        }
    }

    protected final void set(WeakDirectedHashGraph<N> that){
        this.lexicon = that.lexicon;
    }

    protected Lexicon<N,N> getLexicon(){
        return lexicon;
    }

    @Override
    public void putNode(N node) {
        lexicon.putBeing(node);
    }

    @Override
    public void removeNode(N node) {
        for(N it : getNodes()){
            detach(it,node);
        }
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
        return lexicon.getBeing();
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
        dest = lexicon.putBeing(dest);
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

    @Override
    public Collection<N> getUpstreamNodes(N node) {
        List<N> nodes = new ArrayList<>();
        for(N it : getNodes()){
            if(linked(it,node))
                nodes.add(it);
        }
        return nodes;
    }

    @Override
    public N getUpstreamNode(N node) {
        for(N it : getNodes()){
            if(linked(it,node))
                return it;
        }
        return null;
    }

    @Override
    public int countUpstreamNodes(N node) {
        int counter = 0;
        for(N it : getNodes()){
            if(linked(it,node))
                ++counter;
        }
        return counter;
    }
}
