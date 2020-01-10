package app.modules.graph;

import java.util.Set;

public interface CompleteLexicon<B,T> extends Lexicon<B,T> {
    T putTrait(T trait);
    void removeTrait(T trait);
    Set<T> getTraits();
    T getTrait();
    int countTraits();
    boolean containsTrait(T trait);
    Set<B> getBeings(T trait);
    B getBeing(T trait);
    int countBeings(T trait);
    boolean hasBeing(T trait, B being);
}
