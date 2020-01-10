/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.Collection;

public class ReferenceHashGraph<N, L> {
    private GradedLexicon<N, L, N> lexicon;

    public ReferenceHashGraph(){
        lexicon = new GradedHashLexicon<>();
    }

    protected final void set(ReferenceHashGraph<N, L> that){
        this.lexicon = that.lexicon;
    }

    public N putNode(N node) {
        return lexicon.putBeing(node);
    }

    public Collection<N> getNodes() {
        return lexicon.getBeings();
    }

    public Collection<L> getLinks(N node) {
        return lexicon.getTraits(node);
    }

    public int countNodes() {
        return lexicon.countBeings();
    }

    public int countNodes(N node) {
        return lexicon.countTraits(node);
    }

    public boolean containsNode(N node) {
        return lexicon.containsBeing(node);
    }

    public void link(N quill, L pipe, N dart) {
        quill = lexicon.putBeing(quill);
        dart = lexicon.putBeing(dart);
        lexicon.grantTrait(quill, pipe, dart);
    }

    public boolean linked(N quill, L pipe) {
        return lexicon.hasTrait(quill, pipe);
    }

    public N getNode(N quill, L pipe) {
        return lexicon.getGrade(quill, pipe);
    }
}
