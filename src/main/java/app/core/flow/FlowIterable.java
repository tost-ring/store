package app.core.flow;

import app.core.suite.Glass;
import app.core.suite.Subject;
import app.core.suite.Suite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FlowIterable<T> extends Iterable<T>{
    FlowIterator<T> iterator();

    default <F extends T> FlowIterable<F> filter(Class<F> requestedType) {
        return () -> new FlowIterator<F>() {
            Iterator<T> origin = iterator();
            F lastFound = null;

            @Override
            public boolean hasNext() {
                while (origin.hasNext()) {
                    Object o = origin.next();
                    if(requestedType.isInstance(o)) {
                        lastFound = requestedType.cast(o);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public F next() {
                return lastFound;
            }
        };
    }

    default <F extends T> FlowIterable<F> filter(Glass<? super F, F> requestedType) {
        return () -> new FlowIterator<>() {
            Iterator<T> origin = iterator();
            F lastFound = null;

            @Override
            public boolean hasNext() {
                while (origin.hasNext()) {
                    Object o = origin.next();
                    if(requestedType.isInstance(o)) {
                        lastFound = requestedType.cast(o);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public F next() {
                return lastFound;
            }
        };
    }

    default FlowIterable<T> filter(Predicate<T> predicate) {
        return () -> new FlowIterator<>() {
            Iterator<T> origin = iterator();
            T lastFound = null;

            @Override
            public boolean hasNext() {
                while (origin.hasNext()) {
                    T t = origin.next();
                    if(predicate.test(t)) {
                        lastFound = t;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                return lastFound;
            }
        };
    }

    static<I> FlowIterable<I> empty() {
        return FlowIterator::empty;
    }

    default<O> FlowIterable<O> map(Function<T, O> function) {
        return () -> new FlowIterator<>() {
            Iterator<T> origin = iterator();

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

    default FlowIterable<T> skip(int from, int to) {
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
