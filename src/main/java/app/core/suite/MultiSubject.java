package app.core.suite;

import app.core.suite.util.*;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

class MultiSubject implements Subject {

    private final Chain chain;

    MultiSubject() {
        this.chain = new Chain();
    }

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        chain.putLast(key, value);
        return this;
    }

    @Override
    public Subject put(Object element) {
        return put(element, element);
    }

    @Override
    public Subject put(Object key, Object value) {
        chain.putLastIfAbsent(key, value);
        return this;
    }

    @Override
    public Subject add(Object element) {
        chain.putLast(new Suite.Add(), element);
        return this;
    }

    @Override
    public Subject unset() {
        chain.clear();
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
        return chain.getFirst().subject.key();
    }

    @Override
    public Subject prime() {
        return chain.getFirst().subject;
    }

    @Override
    public Subject recent() {
        return chain.getLast().subject;
    }

    @Override
    public Subject get(Object key) {
        return chain.get(key).subject;
    }

    @Override
    public Object direct() {
        return chain.getFirst().subject.direct();
    }
    
    @Override
    public <B> B asExpected() {
        return chain.getFirst().subject.asExpected();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        return chain.getFirst().subject.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        return chain.getFirst().subject.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        return chain.getFirst().subject.asGiven(requestedType, reserve);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        return chain.getFirst().subject.asGiven(requestedType, reserve);
    }

    @Override
    public <B> B orGiven(B reserve) {
        return chain.getFirst().subject.orGiven(reserve);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return chain.getFirst().subject.orDo(supplier);
    }

    @Override
    public boolean assigned(Class<?> type) {
        return chain.getFirst().subject.assigned(type);
    }

    @Override
    public boolean settled() {
        return !chain.isEmpty();
    }

    @Override
    public int size() {
        return chain.size();
    }

    @Override
    public FluidSubject front() {
        return chain;
    }

    @Override
    public FluidSubject reverse() {
        return () -> chain.iterator(true);
    }

    @Override
    public Subject upgradeToIterable() {
        return this;
    }

    @Override
    public String toString() {
        return chain.toString();
    }

    @Override
    public int hashCode() {
        return Suite.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subject && Suite.equals(this, (Subject)obj);
    }

    @Override
    public Subject setAt(Slot slot, Object element) {
        return setAt(slot, element, element);
    }

    @Override
    public Subject setAt(Slot slot, Object key, Object value) {
        if(slot == Slot.PRIME) {
            chain.putFirst(key, value);
        } else if(slot == Slot.RECENT) {
            chain.putLast(key, value);
        } else {
            if(slot instanceof SlotBefore) {
                Link link = chain.get(((SlotBefore) slot).key);
                if(link == chain.ward) {
                    throw new NoSuchElementException();
                } else chain.put(link, key, value);
            } else if(slot instanceof SlotAfter) {
                Link link = chain.get(((SlotAfter) slot).key);
                if(link == chain.ward) {
                    throw new NoSuchElementException();
                } else chain.put(link.back, key, value);
            }
        }
        return this;
    }

    @Override
    public Subject putAt(Slot slot, Object element) {
        return putAt(slot, element, element);
    }

    @Override
    public Subject putAt(Slot slot, Object key, Object value) {
        if(slot == Slot.PRIME) {
            chain.putFirstIfAbsent(key, value);
        } else if(slot == Slot.RECENT) {
            chain.putLastIfAbsent(key, value);
        } else {
            if(slot instanceof SlotBefore) {
                chain.putIfAbsent(chain.get(((SlotBefore) slot).key), key, value);
            } else if(slot instanceof SlotAfter) {
                chain.putIfAbsent(chain.get(((SlotAfter) slot).key).back, key, value);
            }
        }
        return this;
    }

    @Override
    public Subject addAt(Slot slot, Object element) {
        return setAt(slot, new Suite.Add() ,element);
    }
}
