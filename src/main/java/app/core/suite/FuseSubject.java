package app.core.suite;

import app.core.suite.util.FluidSubject;
import app.core.suite.util.Glass;

import java.util.function.Supplier;

class FuseSubject implements Subject {

    private Subject subject;
    private boolean active = true;

    FuseSubject(Subject subject) {
        this.subject = subject;
    }

    private void safe() {
        if(active) {
            subject = ZeroSubject.getInstance().insetAll(subject.front());
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
    public boolean assigned(Class<?> type) {
        return subject.assigned(type);
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
        return subject.front();
    }

    @Override
    public FluidSubject reverse() {
        return subject.reverse();
    }

    @Override
    public Subject upgradeToIterable() {
        subject =  subject.upgradeToIterable();
        return this;
    }

    @Override
    public boolean fused() {
        return true;
    }

    @Override
    public Subject setAt(Slot slot, Object element) {
        safe();
        return subject.setAt(slot, element);
    }

    @Override
    public Subject setAt(Slot slot, Object key, Object value) {
        safe();
        return subject.setAt(slot, key, value);
    }

    @Override
    public Subject putAt(Slot slot, Object element) {
        safe();
        return subject.putAt(slot, element);
    }

    @Override
    public Subject putAt(Slot slot, Object key, Object value) {
        safe();
        return subject.putAt(slot, key, value);
    }

    @Override
    public Subject addAt(Slot slot, Object element) {
        safe();
        return subject.addAt(slot, element);
    }
}
