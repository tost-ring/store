package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;

import java.util.function.Supplier;

public class ZeroSubject implements Subject {

    private static ZeroSubject instance = new ZeroSubject();

    public static ZeroSubject getInstance() {
        return instance;
    }

    private ZeroSubject() {}

    @Override
    public Subject set(Object element) {
        return new PrimeSubject(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return new PrimeSubject(key, value);
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
    public<B> B gsg(B substitute) {
        throw new UnsupportedOperationException("Self upgrade in goc method is not supported for ZeroSubject");
    }

    @Override
    public<B> B gsg(Object key, B substitute) {
        throw new UnsupportedOperationException("Self upgrade in goc method is not supported for ZeroSubject");
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

    @Override
    public FlowCollection<Object> keys() {
        return new FlowArrayList<>();
    }

    @Override
    public FlowCollection<Object> values() {
        return new FlowArrayList<>();
    }

    @Override
    public String toString() {
        return "Subject{}";
    }
}
