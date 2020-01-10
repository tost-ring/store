package app.modules.graph;

import java.util.Collection;

public interface GradedLexicon<B, T, G> extends GrantlessLexicon<B, T> {
    void grantTrait(B being, T trait, G grade);
    G getGrade(B being, T trait);
    Collection<G> getGrades(B being);
}
