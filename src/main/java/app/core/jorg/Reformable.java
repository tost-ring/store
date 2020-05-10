package app.core.jorg;

import app.core.suite.Subject;

public interface Reformable {
    default void reform(Subject sub) {
        StandardReformer.reform(this, sub);
    }
}
