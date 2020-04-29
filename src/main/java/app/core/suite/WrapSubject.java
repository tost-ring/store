package app.core.suite;

import app.core.fluid.Fluid;
import app.core.fluid.FluidIterator;
import app.core.fluid.FluidSubject;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WrapSubject implements Subject {

    private Subject subject;

    WrapSubject() {
        subject = ZeroSubject.getInstance();
    }

    WrapSubject(Subject subject) {
        this.subject = subject;
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
    public Subject put(Object value) {
        subject = subject.put(value);
        return this;
    }

    @Override
    public Subject put(Object key, Object value) {
        subject = subject.put(key, value);
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
    public Subject unset(Object key, Object value) {
        subject = subject.unset(key, value);
        return this;
    }

    @Override
    public Subject prime() {
        return new WrapSubject(subject.prime());
    }

    @Override
    public Subject recent() {
        return new WrapSubject(subject.recent());
    }

    @Override
    public Subject get(Object key) {
        return new WrapSubject(subject.get(key));
    }

    @Override
    public Subject key() {
        return new WrapSubject(subject.key());
    }

    @Override
    public Object direct() {
        return subject.direct();
    }

    @Override
    public <B> B asExpected() {
        return subject.asExpected();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        return subject.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        return subject.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B substitute) {
        return subject.asGiven(requestedType, substitute);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
        return subject.asGiven(requestedType, substitute);
    }

    @Override
    public <B> B orGiven(B substitute) {
        return subject.orGiven(substitute);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return subject.orDo(supplier);
    }

    @Override
    public boolean assigned(Class<?> type) {
        return subject.assigned(type);
    }

    @Override
    public Subject getSaved(Object key, Object reserve) {
        Subject saved = subject.get(key);
        if(saved.settled())return new WrapSubject(saved);
        subject = subject.set(key, reserve);
        return get(key);
    }

    @Override
    public Subject getDone(Object key, Supplier<?> supplier) {
        Subject done = subject.get(key);
        if(done.settled())return new WrapSubject(done);
        subject = subject.set(key, supplier.get());
        return get(key);
    }

    @Override
    public Subject getDone(Object key, Function<Subject, ?> function, Subject argument) {
        Subject done = subject.get(key);
        if(done.settled())return new WrapSubject(done);
        subject = subject.set(key, function.apply(argument));
        return get(key);
    }

    @Override
    public Subject take(Object key) {
        Subject taken = get(key);
        subject = subject.unset(key);
        return taken;
    }

    @Override
    public boolean settled() {
        return subject.settled();
    }

    @Override
    public int size() {
        return subject.size();
    }

    @Override
    public FluidSubject front() {
        return () -> new FluidIterator<>() {
            final FluidIterator<Subject> subIt = subject.front().iterator();

            @Override
            public boolean hasNext() {
                return subIt.hasNext();
            }

            @Override
            public Subject next() {
                return subIt.next();
            }
        };
    }

    @Override
    public FluidSubject reverse() {
        return () -> new FluidIterator<>() {
            final FluidIterator<Subject> subIt = subject.front().iterator();

            @Override
            public boolean hasNext() {
                return subIt.hasNext();
            }

            @Override
            public Subject next() {
                return subIt.next();
            }
        };
    }

    @Override
    public Subject setAll(Iterable<Subject> iterable) {
        subject = subject.setAll(iterable);
        return this;
    }

    @Override
    public Subject putAll(Iterable<Subject> iterable) {
        subject = subject.putAll(iterable);
        return this;
    }

    @Override
    public boolean fused() {
        return subject.fused();
    }

    @Override
    public boolean homogeneous() {
        return true;
    }

    @Override
    public String toString() {
        return subject.toString();
    }
}
