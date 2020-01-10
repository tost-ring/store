package app.modules.graph;

import java.util.Set;

public class CompleteHashLexicon<B,T> extends HashLexicon<B,T> implements CompleteLexicon<B,T> {
    private Lexicon<T,B> antiLexicon;

    public CompleteHashLexicon(){
        super();
        antiLexicon = new HashLexicon<>();
    }

    @Override
    public void removeBeing(B being){
        for(T it : getTraits(being)){
            antiLexicon.loseTrait(it,being);
        }
        super.removeBeing(being);
    }

    @Override
    public T putTrait(T trait) {
        return antiLexicon.putBeing(trait);
    }

    @Override
    public void removeTrait(T trait) {
        for(B it : antiLexicon.getTraits(trait)){
            super.loseTrait(it,trait);
        }
        antiLexicon.removeBeing(trait);
    }

    @Override
    public Set<T> getTraits() {
        return antiLexicon.getBeings();
    }

    @Override
    public T getTrait() {
        return antiLexicon.getBeing();
    }

    @Override
    public int countTraits() {
        return antiLexicon.countBeings();
    }

    @Override
    public boolean containsTrait(T trait) {
        return antiLexicon.containsBeing(trait);
    }

    @Override
    public Set<B> getBeings(T trait) {
        return antiLexicon.getTraits(trait);
    }

    @Override
    public B getBeing(T trait) {
        return antiLexicon.getTrait(trait);
    }

    @Override
    public int countBeings(T trait) {
        return antiLexicon.countTraits(trait);
    }

    @Override
    public boolean hasBeing(T trait, B being) {
        return antiLexicon.hasTrait(trait,being);
    }

    @Override
    public void grantTrait(B being, T trait) {
        super.grantTrait(being, trait);
        antiLexicon.grantTrait(trait, being);
    }

    @Override
    public void loseTrait(B being, T trait){
        super.loseTrait(being,trait);
        antiLexicon.loseTrait(trait,being);
    }
}
