package app.core.suite;

import app.core.flow.FlowIterable;
import app.core.flow.FlowIterator;

import java.util.function.Supplier;
import java.util.stream.Stream;

class FuseSubject implements Subject {

    private Subject subject;

    FuseSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object element) {
        return ZeroSubject.getInstance().met(subject).set(element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return ZeroSubject.getInstance().met(subject).set(key, value);
    }

    @Override
    public Subject sit(Object value) {
        return ZeroSubject.getInstance().met(subject).sit(value);
    }

    @Override
    public Subject sit(Object key, Object value) {
        return ZeroSubject.getInstance().met(subject).sit(key, value);
    }

    @Override
    public <B> Subject setNew(Class<B> key) {
        return ZeroSubject.getInstance().met(subject).setNew(key);
    }

    @Override
    public Subject add(Object element) {
        return ZeroSubject.getInstance().met(subject).add(element);
    }

    @Override
    public Subject unset() {
        return ZeroSubject.getInstance().met(subject).unset();
    }

    @Override
    public Subject unset(Object key) {
        return ZeroSubject.getInstance().met(subject).unset(key);
    }

    @Override
    public <B> B get() {
        return subject.get();
    }

    @Override
    public <B> B get(Object key) {
        return subject.get(key);
    }

    @Override
    public <B> B getAs(Class<B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B getAs(Object key, Class<B> requestedType) {
        return subject.getAs(key, requestedType);
    }

    @Override
    public <B> B getAs(Object key, Glass<? super B, B> requestedType) {
        return subject.getAs(key, requestedType);
    }

    @Override
    public <B> B god(B substitute) {
        return subject.god(substitute);
    }

    @Override
    public <B> B god(Object key, B substitute) {
        return subject.god(key, substitute);
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return subject.godAs(substitute, requestedType);
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return subject.godAs(substitute, requestedType);
    }

    @Override
    public <B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
    }

    @Override
    public <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
    }

    @Override
    public <B> B goMake(Supplier<B> supplier) {
        return subject.goMake(supplier);
    }

    @Override
    public <B> B goMake(Object key, Supplier<B> supplier) {
        return subject.goMake(key, supplier);
    }

    @Override
    public <B> B getAsGiven(Class<B> key) {
        return subject.getAsGiven(key);
    }

    @Override
    public <B> B goNew(Class<B> classKey) {
        return subject.goNew(classKey);
    }

    @Override
    public boolean is() {
        return subject.is();
    }

    @Override
    public boolean isAsStated(Class<?> checkedType) {
        return subject.isAsStated(checkedType);
    }

    @Override
    public boolean is(Object key) {
        return subject.is(key);
    }

    @Override
    public boolean isAsStated(Object key, Class<?> classFilter) {
        return subject.isAsStated(key, classFilter);
    }

    @Override
    public boolean are(Object... keys) {
        return subject.are(keys);
    }

    public int size() {
        return subject.size();
    }

    @Override
    public <K> K getKey() {
        return subject.getKey();
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        return subject.godKey(substitute, requestedType);
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        return subject.godKey(substitute, requestedType);
    }

    @Override
    public Subject met(Subject that) {
        return ZeroSubject.getInstance().met(subject).met(that);
    }

    @Override
    public Subject met(Subject that, Object... keys) {
        return ZeroSubject.getInstance().met(subject).met(that, keys);
    }

    @Override
    public Subject mix(Subject source, Object... keys) {
        return ZeroSubject.getInstance().met(subject).mix(source, keys);
    }

    @Override
    public Stream<Subject> stream() {
        return subject.stream();
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return subject.iterator();
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return subject.keys(lastFirst);
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return subject.values(lastFirst);
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return subject.reverse();
    }
}
