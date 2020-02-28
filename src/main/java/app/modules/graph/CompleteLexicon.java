package app.modules.graph;

import java.util.Collection;
import java.util.Set;

public interface CompleteLexicon<B,T> extends Lexicon<B,T> {
    T putTrait(T trait);
    void removeTrait(T trait);
    Collection<T> getTraits();
    T getTrait();
    int countTraits();
    boolean containsTrait(T trait);
    Collection<B> getBeings(T trait);
    B getBeing(T trait);
    int countBeings(T trait);
    boolean hasBeing(T trait, B being);
}
