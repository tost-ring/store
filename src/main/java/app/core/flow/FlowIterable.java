package app.core.flow;

public interface FlowIterable<T> extends Iterable<T>{
    FlowIterator<T> iterator();
    default FlowArrayList<T> toFAL() {
        return new FlowArrayList<>(this);
    }
    default FlowHashSet<T> toFHS() {
        return new FlowHashSet<>(this);
    }
}
