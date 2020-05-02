package app.core.fluid;

import app.core.suite.Glass;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Fluid<T> extends Iterable<T>{
    FluidIterator<T> iterator();

    default Cascade<T> cascade() {
        return new Cascade<>(iterator());
    }

    default <F extends T> Fluid<F> filter(Class<F> requestedType) {
        return () -> new FluidIterator<F>() {
            final Iterator<T> origin = iterator();
            F next = null;
            boolean nextFound = false;

            @Override
            public boolean hasNext() {
                if(nextFound) return true;
                while (origin.hasNext()) {
                    Object o = origin.next();
                    if(requestedType.isInstance(o)) {
                        next = requestedType.cast(o);
                        nextFound = true;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public F next() {
                nextFound = false;
                return next;
            }
        };
    }

    default <F extends T> Fluid<F> filter(Glass<? super F, F> requestedType) {
        return () -> new FluidIterator<>() {
            final Iterator<T> origin = iterator();
            F next = null;
            boolean nextFound = false;

            @Override
            public boolean hasNext() {
                if(nextFound) return true;
                while (origin.hasNext()) {
                    Object o = origin.next();
                    if(requestedType.isInstance(o)) {
                        next = requestedType.cast(o);
                        nextFound = true;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public F next() {
                nextFound = false;
                return next;
            }
        };
    }

    default Fluid<T> filter(Predicate<T> predicate) {
        return () -> new FluidIterator<>() {
            final Iterator<T> origin = iterator();
            T next = null;
            boolean nextFound = false;

            @Override
            public boolean hasNext() {
                if(nextFound) return true;
                while (origin.hasNext()) {
                    T t = origin.next();
                    if(predicate.test(t)) {
                        next = t;
                        nextFound = true;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                nextFound = false;
                return next;
            }
        };
    }

    static<I> Fluid<I> empty() {
        return FluidIterator::empty;
    }

    default<O> Fluid<O> map(Function<T, O> function) {
        return () -> new FluidIterator<>() {
            final Iterator<T> origin = iterator();

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public O next() {
                return function.apply(origin.next());
            }
        };
    }

    default Fluid<T> skip(int from, int to) {
        return filter(new Predicate<T>() {
            int counter = 0;

            @Override
            public boolean test(T t) {
                ++counter;
                return counter <= from || counter > to;
            }
        });
    }


    default List<T> toList() {
        List<T> list = new ArrayList<>();
        forEach(list::add);
        return list;
    }

    default Subject toSubject(Iterable<?> keysSource) {
        Iterator<?> v = iterator();
        Iterator<?> k = keysSource.iterator();
        Subject subject = Suite.set();
        while (v.hasNext() && k.hasNext()) {
            subject.set(k.next(), v.next());
        }
        return subject;
    }

    default String toString(String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<T> it = iterator();
        while(it.hasNext()) {
            stringBuilder.append(it.next());
            if(it.hasNext())stringBuilder.append(separator);
        }
        return stringBuilder.toString();
    }
}
