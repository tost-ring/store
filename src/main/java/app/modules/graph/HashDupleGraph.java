/**
 * Created by LPO on 08.09.2018
 */

package app.modules.graph;

import java.util.Collection;

public class HashDupleGraph<B, W> implements DupleGraph<B, W> {

    private CompleteLexicon<B, W> lexicon;

    public HashDupleGraph(){
        lexicon = new CompleteHashLexicon<>();
    }

    @Override
    public void putBlack(B black) {
        lexicon.putBeing(black);
    }

    @Override
    public void putWhite(W white) {
        lexicon.putTrait(white);
    }

    @Override
    public void removeBlack(B black) {
        lexicon.removeBeing(black);
    }

    @Override
    public void removeWhite(W white) {
        lexicon.removeTrait(white);
    }

    @Override
    public Collection<B> getBlacks() {
        return lexicon.getBeings();
    }

    @Override
    public Collection<W> getWhites() {
        return lexicon.getTraits();
    }

    @Override
    public Collection<B> getBlacks(W white) {
        return lexicon.getBeings(white);
    }

    @Override
    public Collection<W> getWhites(B black) {
        return lexicon.getTraits(black);
    }

    @Override
    public B getBlack() {
        return lexicon.getBeing();
    }

    @Override
    public W getWhite() {
        return lexicon.getTrait();
    }

    @Override
    public B getBlack(W white) {
        return lexicon.getBeing(white);
    }

    @Override
    public W getWhite(B black) {
        return lexicon.getTrait(black);
    }

    @Override
    public int countBlacks() {
        return lexicon.countBeings();
    }

    @Override
    public int countWhites() {
        return lexicon.countTraits();
    }

    @Override
    public int countBlacks(W white) {
        return lexicon.countBeings(white);
    }

    @Override
    public int countWhites(B black) {
        return lexicon.countTraits(black);
    }

    @Override
    public boolean containsBlack(B black) {
        return lexicon.containsBeing(black);
    }

    @Override
    public boolean containsWhite(W white) {
        return lexicon.containsTrait(white);
    }

    @Override
    public void link(B black, W white) {
        black = lexicon.putBeing(black);
        white = lexicon.putTrait(white);
        lexicon.grantTrait(black, white);
    }

    @Override
    public void detach(B black, W white) {
        lexicon.loseTrait(black, white);
    }

    @Override
    public boolean linked(B black, W white) {
        return lexicon.hasTrait(black, white);
    }
}
