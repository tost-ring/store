package app.core.fluid;

import java.util.Iterator;

public interface FluidIterator<T> extends Iterator<T> {

    default Cascade<T> cascade() {
        return new Cascade<>(this);
    }

    static<I> FluidIterator<I> empty() {
        return new FluidIterator<>() {

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
