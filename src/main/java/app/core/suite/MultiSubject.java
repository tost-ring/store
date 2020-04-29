package app.core.suite;

import app.core.fluid.*;

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
        chain.put(new Suite.Add(), element);
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
        return chain.getFirst().key();
    }

    @Override
    public Subject prime() {
        return chain.getFirst();
    }

    @Override
    public Subject recent() {
        return chain.getLast();
    }

    @Override
    public Subject get(Object key) {
        return chain.get(key);
    }

    @Override
    public Object direct() {
        return chain.getFirst().direct();
    }
    
    @Override
    public <B> B asExpected() {
        return chain.getFirst().asExpected();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        return chain.getFirst().asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        return chain.getFirst().asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        return chain.getFirst().asGiven(requestedType, reserve);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        return chain.getFirst().asGiven(requestedType, reserve);
    }

    @Override
    public <B> B orGiven(B reserve) {
        return chain.getFirst().orGiven(reserve);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return chain.getFirst().orDo(supplier);
    }

    @Override
    public boolean assigned(Class<?> type) {
        return chain.getFirst().assigned(type);
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
}
