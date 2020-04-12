package app.core.suite;

import app.core.flow.FlowIterable;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Subject extends Subjective {

    Subject set(Object element);
    Subject set(Object key, Object value);
    Subject put(Object element);
    Subject put(Object key, Object value);
    Subject add(Object element);
//    add(Object key, Object value) upgrade do grafu ?

    default Subject unset() {
        return ZeroSubject.getInstance();
    }

    Subject unset(Object key); // Usuń wartości pod kluczem $key
    Subject unset(Object key, Object value); // Usuń wartość $value pod kluczem $key

    Subject key();
    Subject prime();
    Subject recent();
    Subject get(Object key);

    Object direct();
    <B> B asExpected();
    <B> B asGiven(Class<B> requestedType);
    <B> B asGiven(Glass<? super B, B> requestedType);
    <B> B asGiven(Class<B> requestedType, B substitute);
    <B> B asGiven(Glass<? super B, B> requestedType, B substitute);
    <B> B orGiven(B substitute);
    <B> B orDo(Supplier<B> supplier);
    boolean isIn(Class<?> type);

    default Subject getSaved(Object key, Object substitute) {
        throw new UnsupportedOperationException("Not implemented in heterogeneous subject");
    }
    default Subject getDone(Object key, Supplier<?> supplier) {
        throw new UnsupportedOperationException("Not implemented in heterogeneous subject");
    }
    default Subject take(Object key) {
        throw new UnsupportedOperationException("Not implemented in heterogeneous subject");
    }

    boolean settled();
    int size();

    Stream<Subject> stream();

    FlowIterable<Subject> front();
    FlowIterable<Subject> reverse();

    FlowIterable<Object> values(boolean lastFirst);

    FlowIterable<Object> keys(boolean lastFirst);

    default FlowIterable<Object> values() {
        return values(false);
    }

    default FlowIterable<Object> keys() {
        return keys(false);
    }

//    default<K, V> Sub<K, V> sub(Class<K> keyType, Class<V> valueType) {
//        return new Sub<>(keyType, valueType).setAll(this.front());
//    }

    default boolean fused() {
        return false;
    }

    default boolean homogeneous() {
        return false;
    }

    default Subject setAll(Iterable<Subject> iterable) {
        Subject subject = this;
        for(Subject it : iterable) {
            subject = subject.set(it.key().direct(), it.direct());
        }
        return subject;
    }

    default Subject putAll(Iterable<Subject> iterable) {
        Subject subject = this;
        for(Subject it : iterable) {
            subject = subject.put(it.key().direct(), it.direct());
        }
        return subject;
    }

    @Override
    default Subject toSubject() {
        return this;
    }

    @Override
    default void fromSubject(Subject subject) {
        putAll(subject.front());
    }
}
