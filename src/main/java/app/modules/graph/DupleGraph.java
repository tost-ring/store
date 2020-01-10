package app.modules.graph;

import java.util.Collection;

public interface DupleGraph<B, W> {
    void putBlack(B black);
    void putWhite(W white);
    void removeBlack(B black);
    void removeWhite(W white);
    Collection<B> getBlacks();
    Collection<W> getWhites();
    Collection<B> getBlacks(W white);
    Collection<W> getWhites(B black);
    B getBlack();
    W getWhite();
    B getBlack(W white);
    W getWhite(B black);
    int countBlacks();
    int countWhites();
    int countBlacks(W white);
    int countWhites(B black);
    boolean containsBlack(B black);
    boolean containsWhite(W white);
    void link(B black, W white);
    void detach(B black, W white);
    boolean linked(B black, W white);
}
