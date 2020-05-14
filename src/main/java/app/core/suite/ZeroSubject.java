package app.core.suite;

import app.core.suite.util.Glass;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

class ZeroSubject implements Subject {

    private static final ZeroSubject instance = new ZeroSubject();

    static ZeroSubject getInstance() {
        return instance;
    }

    private ZeroSubject() {}

    @Override
    public Subject set(Object element) {
        return new BubbleSubject(element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return new CoupleSubject(key, value);
    }

    @Override
    public Subject put(Object element) {
        return set(element);
    }

    @Override
    public Subject put(Object key, Object value) {
        return set(key, value);
    }

    @Override
    public Subject add(Object element) {
        return new CoupleSubject(new Suite.Add(), element);
    }

    @Override
    public Subject unset(Object key) {
        return this;
    }

    @Override
    public Subject unset(Object key, Object value) {
        return this;
    }

    @Override
    public Subject prime() {
        return this;
    }

    @Override
    public Subject recent() {
        return this;
    }

    @Override
    public Subject get(Object key) {
        return this;
    }

    @Override
    public Subject key() {
        return this;
    }

    @Override
    public Object direct() {
        return null;
    }

    @Override
    public <B> B asExpected() {
        throw new NoSuchElementException("ZeroSubject contains no values");
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        throw new NoSuchElementException("ZeroSubject contains no values");
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        throw new NoSuchElementException("ZeroSubject contains no values");
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B substitute) {
        return substitute;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
        return substitute;
    }

    @Override
    public <B> B orGiven(B reserve) {
        return reserve;
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return supplier.get();
    }

    @Override
    public boolean assigned(Class<?> type) {
        return false;
    }

    @Override
    public boolean settled() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Subject iterable() {
        return new MultiSubject();
    }

    @Override
    public String toString() {
        return "[]";
    }
}
