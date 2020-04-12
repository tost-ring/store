package app.core.suite;

import app.core.flow.FlowIterable;

import java.util.function.Supplier;
import java.util.stream.Stream;

class FuseSubject implements Subject {

    private Subject subject;
    private boolean active = true;

    FuseSubject(Subject subject) {
        this.subject = subject;
    }

    private void safe() {
        if(active) {
            subject = ZeroSubject.getInstance().setAll(subject.front());
            active = false;
        }
    }

    @Override
    public Subject set(Object element) {
        safe();
        return subject.set(element);
    }

    @Override
    public Subject set(Object key, Object value) {
        safe();
        return subject.set(key, value);
    }

    @Override
    public Subject put(Object element) {
        safe();
        return subject.put(element);
    }

    @Override
    public Subject put(Object key, Object value) {
        safe();
        return subject.put(key, value);
    }

    @Override
    public Subject add(Object element) {
        safe();
        return subject.add(element);
    }

    @Override
    public Subject unset(Object key) {
        safe();
        return subject.unset(key);
    }

    @Override
    public Subject unset(Object key, Object value) {
        safe();
        return subject.unset(key, value);
    }

    @Override
    public Subject prime() {
        return subject.prime();
    }

    @Override
    public Subject recent() {
        return subject.recent();
    }

    @Override
    public Subject get(Object key) {
        return subject.get(key);
    }

    @Override
    public Subject key() {
        return subject.key();
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
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        return subject.asGiven(requestedType, reserve);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        return subject.asGiven(requestedType, reserve);
    }

    @Override
    public <B> B orGiven(B reserve) {
        return subject.orGiven(reserve);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return subject.orDo(supplier);
    }

    @Override
    public boolean isIn(Class<?> type) {
        return subject.isIn(type);
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
    public Stream<Subject> stream() {
        return subject.stream();
    }

    @Override
    public FlowIterable<Subject> front() {
        return subject.front();
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return subject.reverse();
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return subject.values(lastFirst);
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return subject.keys(lastFirst);
    }

    @Override
    public boolean fused() {
        return true;
    }
}
