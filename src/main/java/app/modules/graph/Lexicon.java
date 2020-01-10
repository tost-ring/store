package app.modules.graph;

public interface Lexicon<B,T> extends GrantlessLexicon<B, T> {
    void grantTrait(B being, T trait);
}
