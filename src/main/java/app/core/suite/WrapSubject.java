package app.core.suite;

import app.core.flow.FlowCollection;

import java.util.function.Supplier;

public class WrapSubject implements Subject {

    private Subject subject;

    public WrapSubject() {
        subject = ZeroSubject.getInstance();
    }

    public WrapSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object value) {
        subject = subject.set(value);
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        subject = subject.set(key, value);
        return this;
    }

    @Override
    public Subject set(Class<?> classKey, Object value) {
        subject = subject.set(classKey, value);
        return this;
    }

    @Override
    public Subject set(Coupon<?> coupon, Object value) {
        subject = subject.set(coupon, value);
        return this;
    }

    @Override
    public Subject set(Object key, Fun fun) {
        subject = subject.set(key, fun);
        return this;
    }

    @Override
    public Subject set(Object key, Tun tun) {
        subject = subject.set(key, tun);
        return this;
    }

    @Override
    public Subject set(Object key, Statement statement) {
        subject = subject.set(key, statement);
        return this;
    }

    @Override
    public Subject sor(Object value) {
        subject = subject.sor(value);
        return this;
    }

    @Override
    public Subject sor(Object key, Object value) {
        subject = subject.sor(key, value);
        return this;
    }

    @Override
    public Subject sor(Class<?> classKey, Object value) {
        subject = subject.sor(classKey, value);
        return this;
    }

    @Override
    public Subject sor(Coupon<?> coupon, Object value) {
        subject = subject.sor(coupon, value);
        return this;
    }

    @Override
    public Subject sor(Object key, Fun fun) {
        subject = subject.sor(key, fun);
        return this;
    }

    @Override
    public Subject sor(Object key, Tun tun) {
        subject = subject.sor(key, tun);
        return this;
    }

    @Override
    public Subject sor(Object key, Statement statement) {
        subject = subject.sor(key, statement);
        return this;
    }

    @Override
    public Subject sok(Object value) {
        subject = subject.sok(value);
        return this;
    }

    @Override
    public Subject sok(Object key, Object value) {
        subject = subject.sok(key, value);
        return this;
    }

    @Override
    public Subject sok(Class<?> classKey, Object value) {
        subject = subject.sok(classKey, value);
        return this;
    }

    @Override
    public Subject sok(Coupon<?> coupon, Object value) {
        subject = subject.sok(coupon, value);
        return this;
    }

    @Override
    public Subject sok(Object key, Fun fun) {
        subject = subject.sok(key, fun);
        return this;
    }

    @Override
    public Subject sok(Object key, Tun tun) {
        subject = subject.sok(key, tun);
        return this;
    }

    @Override
    public Subject sok(Object key, Statement statement) {
        subject = subject.sok(key, statement);
        return this;
    }

    @Override
    public Subject unset(Object key) {
        subject = subject.unset(key);
        return this;
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
    public <B> B get(Class<? super B> classKey) {
        return subject.get(classKey);
    }

    @Override
    public <B> B get(Coupon<B> coupon) {
        return subject.get(coupon);
    }

    @Override
    public <B> B get(Object key, Class<B> classFilter) {
        return subject.get(key, classFilter);
    }

    @Override
    public <B> B get(Object key, Glass<? super B, B> glassFilter) {
        return subject.get(key, glassFilter);
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
    public <B> B god(Object key, Class<B> classFilter, B substitute) {
        return subject.god(key, classFilter, substitute);
    }

    @Override
    public <B> B god(Object key, Glass<? super B, B> glassFilter, B substitute) {
        return subject.god(key, glassFilter, substitute);
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        return subject.gom(supplier);
    }

    @Override
    public <B> B gom(Object key, Supplier<B> supplier) {
        return subject.gom(key, supplier);
    }

    @Override
    public<B> B gos(B substitute) {
        B b = subject.god(null);
        if(b == null) {
            subject = subject.set(substitute);
            return substitute;
        } else {
            return b;
        }
    }

    @Override

    public<B> B gos(Object key, B substitute) {
        B b = subject.god(key,null);
        if(b == null) {
            subject = subject.set(key, substitute);
            return substitute;
        } else {
            return b;
        }
    }

    @Override
    public <B> B gon(Class<B> classKey) {
        return subject.gon(classKey);
    }

    @Override
    public boolean is() {
        return subject.is();
    }

    @Override
    public boolean is(Object key) {
        return subject.is(key);
    }

    @Override
    public <B> boolean is(Object key, Class<B> classFilter) {
        return subject.is(key, classFilter);
    }

    @Override
    public boolean are(Object... keys) {
        return subject.are(keys);
    }

    @Override
    public FlowCollection<Object> keys() {
        return subject.keys();
    }

    @Override
    public FlowCollection<Object> values() {
        return subject.values();
    }

    @Override
    public String toString() {
        return subject.toString();
    }
}
