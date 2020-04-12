package app.core.suite;

import app.core.flow.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
class ChainSubject implements Subject {

    private Chain<Object, Object> chain;

    ChainSubject() {
        this.chain = new Chain<>(true);
    }

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        chain.put(key, value);
        return this;
    }

    @Override
    public Subject put(Object element) {
        return put(element, element);
    }

    @Override
    public Subject put(Object key, Object value) {
        chain.putIfAbsent(key, value);
        return this;
    }

    @Override
    public Subject add(Object element) {
        chain.put(new Object(), element);
        return this;
    }

    @Override
    public Subject unset(Object key) {
        chain.remove(key);
        return this;
    }

    @Override
    public Subject unset(Object key, Object value) {
        chain.remove(key, value);
        return this;
    }

    @Override
    public Subject key() {
        var link = chain.getLastLink();
        return link == null ? ZeroSubject.getInstance() : new BubbleSubject(link.getKey());
    }

    @Override
    public Subject prime() {
        var link = chain.getFirstLink();
        return link == null ? ZeroSubject.getInstance() : new CoupleSubject(link.getKey(), link.getValue());
    }

    @Override
    public Subject recent() {
        var link = chain.getLastLink();
        return link == null ? ZeroSubject.getInstance() : new CoupleSubject(link.getKey(), link.getValue());
    }

    @Override
    public Subject get(Object key) {
        var link = chain.getLink(key);
        return link == null ? ZeroSubject.getInstance() : new CoupleSubject(link.getKey(), link.getValue());
    }

    @Override
    public Object direct() {
        var link = chain.getLastLink();
        return link == null ? null : link.getValue();
    }
    
    @Override
    public <B> B asExpected() {
        var link = chain.getLastLink();
        if(link == null) throw new NoSuchElementException();
        return (B)link.getValue();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        var link = chain.getLastLink();
        if(link == null) throw new NoSuchElementException();
        return (B)link.getValue();
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        var link = chain.getLastLink();
        if(link == null) throw new NoSuchElementException();
        return (B)link.getValue();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        var link = chain.getLastLink();
        if(link == null)return reserve;
        return requestedType.isInstance(link.getValue()) ? requestedType.cast(link.getValue()) : reserve;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        var link = chain.getLastLink();
        if(link == null)return reserve;
        return requestedType.isInstance(link.getValue()) ? requestedType.cast(link.getValue()) : reserve;
    }

    @Override
    public <B> B orGiven(B reserve) {
        var link = chain.getLastLink();
        if(link == null)return reserve;
        return link.getValue() == null ? reserve : (B)link.getValue();
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        var link = chain.getLastLink();
        if(link == null)return supplier.get();
        return link.getValue() == null ? supplier.get() : (B)link.getValue();
    }

    @Override
    public boolean isIn(Class<?> type) {
        var link = chain.getLastLink();
        if(link == null)return false;
        return type.isInstance(link.getValue());
    }

    @Override
    public boolean settled() {
        return !chain.isEmpty();
    }

    @Override
    public int size() {
        return chain.size();
    }

    public Stream<Subject> stream() {
        Spliterator<Subject> spliterator = new Spliterator<>() {
            Iterator<Subject> iterator = front().iterator();

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
                return size();
            }

            @Override
            public int characteristics() {
                return DISTINCT | NONNULL | ORDERED | SIZED;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public FlowIterable<Subject> front() {
        return () -> new FlowIterator<>() {
            Iterator<Map.Entry<Object, Object>> origin = chain.iterator();

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public Subject next() {
                var entry = origin.next();
                return new CoupleSubject(entry.getKey(), entry.getValue());
            }
        };
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return () -> new FlowIterator<>() {
            Iterator<Map.Entry<Object, Object>> origin = chain.iterator(true);

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public Subject next() {
                var entry = origin.next();
                return new CoupleSubject(entry.getKey(), entry.getValue());
            }
        };
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return () -> chain.valuesIterator(lastFirst);
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return () -> chain.keysIterator(lastFirst);
    }

    @Override
    public String toString() {
        return "$" + chain;
    }

    @Override
    public int hashCode() {
        return Suite.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subject && Suite.equals(this, (Subject)obj);
    }
}
