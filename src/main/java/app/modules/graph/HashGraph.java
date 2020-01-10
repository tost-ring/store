/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.*;

public class HashGraph<N> implements WeightlessGraph<N> {
    private Lexicon<N,N> lexicon;

    public HashGraph(){
        lexicon = new HashLexicon<>();
    }

    public HashGraph(WeightlessGraph<N> that){
        this();
        if(that != null) {
            for (N it : that.getNodes()) {
                for(N itt : that.getNodes(it)){
                    link(it,itt);
                }
            }
        }
    }

    protected final void set(HashGraph<N> that){
        this.lexicon = that.lexicon;
    }

    @Override
    public void putNode(N node){
        lexicon.putBeing(node);
    }

    @Override
    public void removeNode(N node){
        for(N it : lexicon.getTraits(node)){
            lexicon.loseTrait(it,node);
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
    public void link(N src, N dest){
        src = lexicon.putBeing(src);
        dest = lexicon.putBeing(dest);
        lexicon.grantTrait(src,dest);
        lexicon.grantTrait(dest,src);
    }

    @Override
    public void detach(N src, N dest){
        lexicon.loseTrait(src,dest);
        lexicon.loseTrait(dest,src);
    }

    @Override
    public boolean linked(N src, N dest){
        return lexicon.hasTrait(src,dest);
    }

    @Override
    public void merge(Graph<N> that) {
        Set<N> linkedNodes = new HashSet<>(that.countNodes());
        for(N it : that.getNodes()){
            for(N itt : that.getNodes(it)){
                if(!linkedNodes.contains(itt))
                    link(it,itt);
            }
            linkedNodes.add(it);
        }
    }
}
