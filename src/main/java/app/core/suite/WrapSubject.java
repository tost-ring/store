package app.core.suite;

import app.core.flow.FlowCollection;
import app.core.suite.transition.Transition;

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
    public Subject set(Coupon<?> coupon, Object value) {
        subject = subject.set(coupon, value);
        return this;
    }

    @Override
    public Subject set(Object key, Transition transition) {
        subject = subject.set(key, transition);
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
    public Subject sor(Coupon<?> coupon, Object value) {
        subject = subject.sor(coupon, value);
        return this;
    }

    @Override
    public Subject sor(Object key, Transition transition) {
        subject = subject.sor(key, transition);
        return this;
    }

    @Override
    public Subject sos(Object value) {
        subject = subject.sos(value);
        return this;
    }

    @Override
    public Subject sos(Object key, Object value) {
        subject = subject.sos(key, value);
        return this;
    }

    @Override
    public Subject sos(Coupon<?> coupon, Object value) {
        subject = subject.sos(coupon, value);
        return this;
    }

    @Override
    public Subject sos(Object key, Transition transition) {
        subject = subject.sos(key, transition);
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
    public <B> B getAs(Class<B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B get(Coupon<B> coupon) {
        return subject.get(coupon);
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
    public <B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
    }

    @Override
    public <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
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
    public<B> B goc(B substitute) {
        B b = subject.god(null);
        if(b == null) {
            subject = subject.set(substitute);
            return substitute;
        } else {
            return b;
        }
    }

    @Override
    public<B> B goc(Object key, B substitute) {
        B b = subject.god(key,null);
        if(b == null) {
            subject = subject.set(key, substitute);
            return substitute;
        } else {
            return b;
        }
    }

    @Override
    public <B> B gac(Class<B> key) {
        return subject.gac(key);
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
    public <B> boolean iso(Class<B> checkedType) {
        return subject.iso(checkedType);
    }

    @Override
    public boolean is(Object key) {
        return subject.is(key);
    }

    @Override
    public <B> boolean iso(Object key, Class<B> classFilter) {
        return subject.iso(key, classFilter);
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
