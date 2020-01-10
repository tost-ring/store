package app.core.suite;

import app.core.flow.FlowCollection;

import java.util.function.Supplier;

public interface Subject {

    enum SetMode {SET_ELSE_THROW, SET_OR_REPLACE, SET_OR_KEEP}
    Object toString = new Object();

    Subject set(Object value);

    Subject set(Object key, Object value);

    Subject set(Class<?> classKey, Object value);

    Subject set(Coupon<?> coupon, Object value);

    Subject set(Object key, Fun fun);

    Subject set(Object key, Tun tun);

    Subject set(Object key, Statement statement);

    Subject sor(Object value);

    Subject sor(Object key, Object value);

    Subject sor(Class<?> classKey, Object value);

    Subject sor(Coupon<?> coupon, Object value);

    Subject sor(Object key, Fun fun);

    Subject sor(Object key, Tun tun);

    Subject sor(Object key, Statement statement);

    Subject sok(Object value);

    Subject sok(Object key, Object value);

    Subject sok(Class<?> classKey, Object value);

    Subject sok(Coupon<?> coupon, Object value);

    Subject sok(Object key, Fun fun);

    Subject sok(Object key, Tun tun);

    Subject sok(Object key, Statement statement);

    Subject unset(Object key);

    <B> B get();

    <B> B get(Object key);

    <B> B get(Class<? super B> classKey);

    <B> B get(Coupon<B> coupon);

    <B> B get(Object key, Class<B> classFilter);

    <B> B get(Object key, Glass<? super B, B> glassFilter);

    <B> B god(B substitute);

    <B> B god(Object key, B substitute);

    <B> B god(Object key, Class<B> classFilter, B substitute);

    <B> B god(Object key, Glass<? super B, B> glassFilter, B substitute);

    <B> B gom(Supplier<B> supplier);

    <B> B gom(Object key, Supplier<B> supplier);

    <B> B gos(B substitute);

    <B> B gos(Object key, B substitute);

    <B> B gon(Class<B> classKey);

    boolean is();

    boolean is(Object key);

    <B>boolean is(Object key, Class<B> classFilter);

    boolean are(Object ... keys);

    FlowCollection<Object> keys();

    FlowCollection<Object> values();

    default Subject forge(Subject source, Object... keys) {
        Subject subject = this;
        for(Object key : keys) {
            subject = subject.sor(key, (Object)source.get(key));
        }
        return subject;
    }

    default Subject forgeExisting(Subject source, Object... keys) {
        Subject subject = this;
        for(Object key : keys) {
            Object o = source.god(key, null);
            if(o != null) {
                subject = subject.sor(key, o);
            }
        }
        return subject;
    }

    default Subject forge(Subject that) {
        Subject subject = this;
        for(Object key : Suite.keys(that)) {
            subject = subject.sor(key, (Object)that.get(key));
        }
        return subject;
    }

}
