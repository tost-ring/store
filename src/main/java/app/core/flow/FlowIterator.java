package app.core.flow;

import java.util.Iterator;

public interface FlowIterator<T> extends Iterator<T> {
    default T nod(T substitute) {
        return hasNext() ? next() : substitute;
    }

    default FlowArrayList<T> collectRemaining() {
        FlowArrayList<T> flowArrayList = new FlowArrayList<>();
        forEachRemaining(flowArrayList::add);
        return flowArrayList;
    }
}
