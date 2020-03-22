package app.core.suite;

import app.core.flow.FlowIterable;
import app.core.flow.FlowIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FilteredSubject implements Subject {

    private Subject subject;
    private Subject filter;

    public FilteredSubject(Subject subject, Subject filter) {
        this.subject = subject;
        this.filter = filter;
    }

    @Override
    public Subject set(Object element) {
        subject = subject.set(element);
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        subject = subject.set(key, value);
        return this;
    }

    @Override
    public Subject sit(Object element) {
        subject = subject.sit(element);
        return this;
    }

    @Override
    public Subject sit(Object key, Object value) {
        subject = subject.sit(key, value);
        return this;
    }

    @Override
    public <B> Subject setNew(Class<B> key) {
        subject = subject.setNew(key);
        return this;
    }

    @Override
    public Subject add(Object element) {
        subject = subject.add(element);
        return this;
    }

    @Override
    public Subject unset() {
        subject = subject.unset();
        return this;
    }

    @Override
    public Subject unset(Object key) {
        subject = subject.unset(key);
        return this;
    }

    @Override
    public <B> B get() {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.get(key);
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B get(Object key) {
        if(filter.is(key)) {
            return subject.get(key);
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B getAs(Class<B> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.getAs(key, requestedType);
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.getAs(key, requestedType);
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B getAs(Object key, Class<B> requestedType) {
        if(filter.is(key)) {
            return subject.getAs(key, requestedType);
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B getAs(Object key, Glass<? super B, B> requestedType) {
        if(filter.is(key)) {
            return subject.getAs(key, requestedType);
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B god(B substitute) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.god(key, substitute);
            }
        }
        return substitute;
    }

    @Override
    public <B> B god(Object key, B substitute) {
        if(filter.is(key)) {
            return subject.god(key, substitute);
        }
        return substitute;
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.godAs(key, substitute, requestedType);
            }
        }
        return substitute;
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.godAs(key, substitute, requestedType);
            }
        }
        return substitute;
    }

    @Override
    public <B> B godAs(Object key, B substitute, Class<B> requestedType) {
        if(filter.is(key)) {
            return subject.godAs(key, substitute, requestedType);
        }
        return substitute;
    }

    @Override
    public <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        if(filter.is(key)) {
            return subject.godAs(key, substitute, requestedType);
        }
        return substitute;
    }

    @Override
    public <B> B goMake(Supplier<B> supplier) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.goMake(key, supplier);
            }
        }
        return supplier.get();
    }

    @Override
    public <B> B goMake(Object key, Supplier<B> supplier) {
        if(filter.is(key)) {
            return subject.goMake(key, supplier);
        }
        return supplier.get();
    }

    @Override
    public <B> B getAsGiven(Class<B> key) {
        if(filter.is(key)) {
            return subject.getAsGiven(key);
        }
        throw new NoSuchElementException();
    }

    @Override
    public <B> B goNew(Class<B> key) {
        if(filter.is(key)) {
            return subject.goNew(key);
        }
        try {
            return key.getConstructor().newInstance();
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + key);
        }
    }

    @Override
    public boolean is() {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.is(key);
            }
        }
        return false;
    }

    @Override
    public boolean isAsStated(Class<?> checkedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return subject.isAsStated(key, checkedType);
            }
        }
        return false;
    }

    @Override
    public boolean is(Object key) {
        return filter.is(key) && subject.is(key);
    }

    @Override
    public boolean isAsStated(Object key, Class<?> checkedType) {
        return filter.is(key) && subject.isAsStated(key, checkedType);
    }

    @Override
    public boolean are(Object... keys) {
        for(var key : keys) {
            if(!filter.is(key) || !subject.is(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> K getKey() {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return (K)key;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return requestedType.isInstance(key) ? requestedType.cast(key) : substitute;
            }
        }
        return substitute;
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        for(var key : subject.keys(false)) {
            if(filter.is(key)) {
                return requestedType.isInstance(key) ? requestedType.cast(key) : substitute;
            }
        }
        return substitute;
    }

    @Override
    public Stream<Subject> stream() {
        Spliterator<Subject> spliterator = new Spliterator<>() {
            Iterator<Subject> iterator = iterator();

            @Override
            public boolean tryAdvance(Consumer<? super Subject> action) {
                if (action == null) throw new NullPointerException();
                if (iterator.hasNext()) {
                    action.accept(iterator.next());
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<Subject> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return DISTINCT | NONNULL | ORDERED;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return new FlowIterator<>() {
            Iterator<Object> origin = subject.keys().iterator();
            Subject next = Suite.set();

            @Override
            public boolean hasNext() {
                while(origin.hasNext()) {
                    var key = origin.next();
                    if(filter.is(key)){
                        next = Suite.set(key, subject.godAs(key, null, Object.class));
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Subject next() {
                return next;
            }
        };
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return () -> new FlowIterator<>() {
            Iterator<Object> origin = subject.keys(lastFirst).iterator();
            Object next = null;

            @Override
            public boolean hasNext() {
                while(origin.hasNext()) {
                    var key = origin.next();
                    if(filter.is(key)){
                        next = key;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Object next() {
                return next;
            }
        };
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return () -> new FlowIterator<>() {
            Iterator<Object> origin = subject.keys(lastFirst).iterator();
            Object next = null;

            @Override
            public boolean hasNext() {
                while(origin.hasNext()) {
                    var key = origin.next();
                    if(filter.is(key)){
                        next = subject.godAs(key, null, Object.class);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Object next() {
                return next;
            }
        };
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return () -> new FlowIterator<>() {
            Iterator<Object> origin = subject.keys(true).iterator();
            Subject next = Suite.set();

            @Override
            public boolean hasNext() {
                while(origin.hasNext()) {
                    var key = origin.next();
                    if(filter.is(key)){
                        next = Suite.set(key, subject.godAs(key, null, Object.class));
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Subject next() {
                return next;
            }
        };
    }
}
