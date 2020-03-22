package app.core.suite;

import app.core.flow.FlowIterable;
import app.core.flow.FlowIterator;

import java.util.function.Supplier;
import java.util.stream.Stream;

class BubbleSubject implements Subject {

    private Object value;

    public BubbleSubject(Object value) {
        this.value = value;
    }

    protected Subject upgrade() {
        return new ChainSubject().set(this, value);
    }

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        if(equals(key)) {
            this.value = value;
            return this;
        } else {
            return upgrade().set(key, value);
        }
    }

    @Override
    public Subject sit(Object element) {
        return sit(element, element);
    }

    @Override
    public Subject sit(Object key, Object value) {
        if(equals(key)) {
            return this;
        } else {
            return upgrade().set(key, value);
        }
    }

    @Override
    public <B> Subject setNew(Class<B> key) {
        try {
            return set(key, key.getConstructor().newInstance());
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + key);
        }
    }

    @Override
    public Subject add(Object element) {
        return upgrade().add(element);
    }

    @Override
    public Subject unset() {
        return ZeroSubject.getInstance();
    }

    @Override
    public Subject unset(Object key) {
        if(equals(key)) {
            return ZeroSubject.getInstance();
        } else {
            return this;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B get() {
        if(value == null) {
            throw new NullPointerException("Missing first value");
        }
        return (B)value;
    }

    @Override
    public <B> B get(Object key) {
        if(equals(key)) {
            return get();
        } else {
            throw new NullPointerException("Missing value associated with key " + key);
        }
    }

    @Override
    public<B> B getAs(Class<B> requestedType) {
        return get();
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        return get();
    }

    @Override
    public<B> B getAs(Object key, Class<B> requestedType) {
        return get(key);
    }

    @Override
    public<B> B getAs(Object key, Glass<? super B, B> requestedType) {
        return get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B god(B substitute) {
        return value != null ? (B)value : substitute;
    }

    @Override
    public<B> B god(Object key, B substitute) {
        return equals(key) ? god(substitute) : substitute;
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return requestedType.isInstance(value) ? requestedType.cast(value) : substitute;
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return requestedType.isInstance(value) ? requestedType.cast(value) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return equals(key) ? godAs(substitute, requestedType) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return equals(key) ? godAs(substitute, requestedType) : substitute;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B goMake(Supplier<B> supplier) {
        return value != null ? (B)value : supplier.get();
    }

    @Override
    public <B> B goMake(Object key, Supplier<B> supplier) {
        return equals(key) ? goMake(supplier) : supplier.get();
    }

    @Override
    public <B> B getAsGiven(Class<B> key) {
        return get(key);
    }

    @Override
    public<B> B goNew(Class<B> classKey) {
        try {
            return classKey.getConstructor().newInstance();
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + classKey);
        }
    }

    @Override
    public boolean is() {
        return value != null;
    }

    @Override
    public boolean isAsStated(Class<?> checkedType) {
        return checkedType.isInstance(value);
    }

    @Override
    public boolean is(Object key) {
        return equals(key) && value != null;
    }

    @Override
    public boolean isAsStated(Object key, Class<?> classFilter){
        return equals(key) && classFilter.isInstance(value);
    }

    @Override
    public boolean are(Object ... keys) {
        for(Object key : keys) {
            if(!is(key))return false;
        }
        return true;
    }

    public int size() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> K getKey() {
        return (K)this;
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        return requestedType.isInstance(this) ? requestedType.cast(this) : substitute;
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        return requestedType.isInstance(this) ? requestedType.cast(this) : substitute;
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return new FlowIterator<>() {
            boolean available = true;

            @Override
            public boolean hasNext() {
                return available;
            }

            @Override
            public Subject next() {
                available = false;
                return Suite.set(this, value);
            }
        };
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return () -> new FlowIterator<>() {
            boolean available = true;

            @Override
            public boolean hasNext() {
                return available;
            }

            @Override
            public Object next() {
                available = false;
                return BubbleSubject.this;
            }
        };
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return () -> new FlowIterator<>() {
            boolean available = true;

            @Override
            public boolean hasNext() {
                return available;
            }

            @Override
            public Object next() {
                available = false;
                return value;
            }
        };
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return this;
    }

    public Stream<Subject> stream() {
        return Stream.of(Suite.set(this, value));
    }

    @Override
    public String toString() {
        return "$[" + super.toString() + "=" + value + "]";
    }
}
