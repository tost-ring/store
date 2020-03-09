package app.core.flow;

import java.util.Iterator;

public interface FlowIterable<T> extends Iterable<T>{
    FlowIterator<T> iterator();
}
