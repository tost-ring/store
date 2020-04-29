package app.core.jorg;

import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.Objects;

public class Crystal {

    public static Crystal init(Object germ) {
        return new Crystal(germ);
    }

    private Object id;
    private final Object germ;
    private final Subject body;
    private boolean ready = false;

    Crystal(Object germ) {
        this.germ = germ;
        body = Suite.set();
    }

    public Crystal(Object id, Object germ) {
        this(germ);
        this.id = id;
    }

    public Object getGerm() {
        return germ;
    }

    public Subject getBody() {
        return body;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public boolean equals(Object o1) {
        return (o1 instanceof Crystal && germ == ((Crystal) o1).germ && Objects.equals(id, ((Crystal) o1).id)) || germ == o1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(germ);
    }

    @Override
    public String toString() {
        return "@[" + id + "]" + germ;
    }

}
