package app.core.suite;

import app.core.suite.util.FluidSubject;
import app.core.jorg.Jorg;
import app.core.jorg.Performable;
import app.core.jorg.Reformable;
import app.core.suite.util.Glass;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Subject extends Performable, Reformable {

    Subject set(Object element);
    Subject set(Object key, Object value);
    Subject put(Object element);
    Subject put(Object key, Object value);
    Subject add(Object element);

    default Subject unset() {
        return ZeroSubject.getInstance();
    }

    Subject unset(Object key); // Usuń wartości pod kluczem $key
    Subject unset(Object key, Object value); // Usuń wartość $value pod kluczem $key

    Subject key();
    Subject prime();
    Subject recent();
    Subject get(Object key);
    default Subject at(Object key) {
        return get(key).asExpected();
    }

    Object direct();
    <B> B asExpected();
    <B> B asGiven(Class<B> requestedType);
    <B> B asGiven(Glass<? super B, B> requestedType);
    <B> B asGiven(Class<B> requestedType, B substitute);
    <B> B asGiven(Glass<? super B, B> requestedType, B substitute);
    <B> B orGiven(B substitute);
    <B> B orDo(Supplier<B> supplier);
    boolean assigned(Class<?> type);

    default Subject getSaved(Object key, Object substitute) {
        throw new UnsupportedOperationException();
    }
    default Subject getDone(Object key, Supplier<?> supplier) {
        throw new UnsupportedOperationException();
    }
    default Subject getDone(Object key, Function<Subject, ?> function, Subject argument) {
        throw new UnsupportedOperationException();
    }
    default Subject take(Object key) {
        throw new UnsupportedOperationException();
    }

    boolean settled();
    int size();

    default FluidSubject front() {
        throw new UnsupportedOperationException();
    }

    default FluidSubject reverse() {
        throw new UnsupportedOperationException();
    }

    Subject iterable();

    default boolean fused() {
        return false;
    }

    default boolean homogeneous() {
        return false;
    }

    default Subject insetAll(Iterable<Subject> iterable) {
        Subject subject = this;
        for(Subject it : iterable) {
            subject = subject.set(it.key().direct(), it.direct());
        }
        return subject;
    }

    default Subject inputAll(Iterable<Subject> iterable) {
        Subject subject = this;
        for(Subject it : iterable) {
            subject = subject.put(it.key().direct(), it.direct());
        }
        return subject;
    }

    default Subject setAll(Iterable<Object> iterable) {
        Subject subject = this;
        for(Object it : iterable) {
            subject = subject.set(it);
        }
        return subject;
    }

    default Subject putAll(Iterable<Object> iterable) {
        Subject subject = this;
        for(Object it : iterable) {
            subject = subject.put(it);
        }
        return subject;
    }

    default Subject addAll(Iterable<Object> iterable) {
        Subject subject = this;
        for(Object it : iterable) {
            subject = subject.add(it);
        }
        return subject;
    }

    @Override
    default Subject perform() {
        return key().assigned(Suite.Add.class) ? Suite.set(Jorg.terminator).insetAll(front()) : this;
    }

    @Override
    default void reform(Subject subject) {
        insetAll(subject.front());
    }
}
