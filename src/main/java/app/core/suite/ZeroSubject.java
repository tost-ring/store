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
    public Subject set(Object value) {
        return new PrimeSubject().set(value, value);
    }

    @Override
    public Subject set(Object key, Object value) {
        return new PrimeSubject().set(key, value);
    }

    @Override
    public Subject set(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            return new PrimeSubject().set(classKey, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
    }

    @Override
    public Subject set(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            return new PrimeSubject().set(coupon, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
    }

    @Override
    public Subject set(Object key, Fun fun) {
        return new PrimeSubject().set(key, fun);
    }

    @Override
    public Subject set(Object key, Tun tun) {
        return new PrimeSubject().set(key, tun);
    }

    @Override
    public Subject set(Object key, Statement statement) {
        return new PrimeSubject().set(key, Fun.make(statement));
    }

    @Override
    public Subject sor(Object value) {
        return set(value);
    }

    @Override
    public Subject sor(Object key, Object value) {
        return set(key, value);
    }

    @Override
    public Subject sor(Class<?> classKey, Object value) {
        return set(classKey, value);
    }

    @Override
    public Subject sor(Coupon<?> coupon, Object value) {
        return set(coupon, value);
    }

    @Override
    public Subject sor(Object key, Fun fun) {
        return set(key, fun);
    }

    @Override
    public Subject sor(Object key, Tun tun) {
        return set(key, tun);
    }

    @Override
    public Subject sor(Object key, Statement statement) {
        return set(key, statement);
    }

    @Override
    public Subject sok(Object value) {
        return set(value);
    }

    @Override
    public Subject sok(Object key, Object value) {
        return set(key, value);
    }

    @Override
    public Subject sok(Class<?> classKey, Object value) {
        return set(classKey, value);
    }

    @Override
    public Subject sok(Coupon<?> coupon, Object value) {
        return set(coupon, value);
    }

    @Override
    public Subject sok(Object key, Fun fun) {
        return set(key, fun);
    }

    @Override
    public Subject sok(Object key, Tun tun) {
        return set(key, tun);
    }

    @Override
    public Subject sok(Object key, Statement statement) {
        return set(key, statement);
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
    public<B> B get(Class<? super B> classKey) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B get(Coupon<B> coupon) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B get(Object key, Class<B> classFilter) {
        throw new NullPointerException("ZeroSubject contains no values");
    }

    @Override
    public<B> B get(Object key, Glass<? super B, B> glassFilter) {
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
    public<B> B god(Object key, Class<B> classFilter, B substitute) {
        return substitute;
    }

    @Override
    public<B> B god(Object key, Glass<? super B, B> glassFilter, B substitute) {
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
    public<B> B gos(B substitute) {
        throw new UnsupportedOperationException("Self upgrade in gos method is not supported for ZeroSubject");
    }

    @Override
    public<B> B gos(Object key, B substitute) {
        throw new UnsupportedOperationException("Self upgrade in gos method is not supported for ZeroSubject");
    }

    @Override
    public<B> B gon(Class<B> classKey) {
        try {
            return classKey.getConstructor().newInstance();
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + classKey);
        }
    }

    @Override
    public boolean is() {
        return false;
    }

    @Override
    public boolean is(Object key) {
        return false;
    }

    @Override
    public <B>boolean is(Object key, Class<B> classFilter){
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
