package app.modules.graph;

import java.util.Collection;
import java.util.Set;

public interface GrantlessLexicon<B,T> {
    B putBeing(B being);
    void removeBeing(B being);
    Set<B> getBeings();
    B getBeing();
    int countBeings();
    boolean containsBeing(B being);
    Collection<T> getTraits(B being);
    T getTrait(B being);
    int countTraits(B being);
    boolean hasTrait(B being, T trait);
    void loseTrait(B being, T trait);
}
