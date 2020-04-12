package app.core.flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface FlowIterator<T> extends Iterator<T> {
    default T next(T substitute) {
        return hasNext() ? next() : substitute;
    }

    default List<T> collectRemaining() {
        List<T> list = new ArrayList<>();
        forEachRemaining(list::add);
        return list;
    }

    static<I> FlowIterator<I> empty() {
        return new FlowIterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public I next() {
                return null;
            }
        };
    }
}
