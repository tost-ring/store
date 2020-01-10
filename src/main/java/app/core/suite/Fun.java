package app.core.suite;

import java.io.Serializable;

@FunctionalInterface
public interface Fun extends Serializable {
    Subject play(Subject subject);
    default Subject gamble(Subject subject) throws Exception {
        return play(subject);
    }
    default Subject play(){
        return play(Suite.set());
    }

    static Fun needless() {
        return s -> s;
    }
    static Fun lossy() {
        return s -> Suite.set();
    }
    static Fun make(Statement statement) {
        return s -> {
            statement.play();
            return s;
        };
    }
}
