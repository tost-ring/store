package app.core.suite;

import app.core.flow.FlowIterator;

import java.util.function.Supplier;
import java.util.stream.Stream;

class ZeroSubject implements Subject {

    private static ZeroSubject instance = new ZeroSubject();

    static ZeroSubject getInstance() {
        return instance;
    }

    private ZeroSubject() {}

    @Override
    public Subject set(Object element) {
        return new CoupleSubject(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return new CoupleSubject(key, value);
    }

    @Override
    public Subject sos(Object element) {
        return set(element);
    }

    @Override
    public Subject sos(Object key, Object value) {
        return set(key, value);
    }

    @Override
    public <B> Subject sen(Class<B> key) {
        try {
            return set(key, key.getConstructor().newInstance());
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + key);
        }
    }

    @Override
    public Subject add(Object element) {
        return new BubbleSubject(element);
    }

    @Override
    public Subject unset() {
        return this;
    }

    @Override
    public Subject unset(Object key) {
        return this;
    }

    @Override
    public<B> B get() {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B get(Object key) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B getAs(Class<B> requestedType) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B getAs(Object key, Class<B> requestedType) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B getAs(Object key, Glass<? super B, B> requestedType) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B god(B substitute) {
        return substitute;
    }

    @Override
    public<B> B god(Object key, B substitute) {
        return substitute;
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return substitute;
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return substitute;
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        return supplier.get();
    }

    @Override
    public <B> B gom(Object key, Supplier<B> supplier) {
        return supplier.get();
    }

    @Override
    public <B> B gac(Class<B> key) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B gon(Class<B> key) {
        try {
            return key.getConstructor().newInstance();
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + key);
        }
    }

    @Override
    public boolean is() {
        return false;
    }

    @Override
    public <B> boolean isi(Class<B> checkedType) {
        return false;
    }

    @Override
    public boolean is(Object key) {
        return false;
    }

    @Override
    public <B>boolean isi(Object key, Class<B> classFilter){
        return false;
    }

    @Override
    public boolean are(Object ... keys) {
        return keys.length == 0;
    }

    public int size() {
        return 0;
    }

    @Override
    public <K> K getKey() {
        throw new NullPointerException("ZeroSubject contains no keys");
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        return substitute;
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        return substitute;
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return new FlowIterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Subject next() {
                return Suite.set();
            }
        };
    }

    public Stream<Subject> stream() {
        return Stream.empty();
    }

    @Override
    public String toString() {
        return "$[]";
    }
}
