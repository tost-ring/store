package app.core.jorg;

import java.util.Objects;

public class Xray {
    private final Object object;
    private String trace;
    private boolean forceTrace;

    Xray(Object object) {
        this(object, false);
    }

    Xray(Object obj, boolean forceTrace) {
        object = obj;
        this.forceTrace = forceTrace;
    }

    public Xray(Object object, String trace) {
        this.object = object;
        this.trace = trace;
        this.forceTrace = true;
    }

    @Override
    public boolean equals(Object o1) {
        return (o1 instanceof Xray && object == ((Xray) o1).object) || object == o1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }

    @Override
    public String toString() {
        return "" + object;
    }

    public Object getObject() {
        return object;
    }

    public String getTrace() {
        return trace;
    }

    public boolean isForceTrace() {
        return forceTrace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public void setForceTrace(boolean forceTrace) {
        this.forceTrace = forceTrace;
    }
}
