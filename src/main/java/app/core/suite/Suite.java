package app.core.suite;

import app.core.flow.FlowCollection;

public final class Suite {


    public static Subject set() {
        return new WrapSubject();
    }
    public static Subject set(Object value) {
        return new WrapSubject(new PrimeSubject().set(value));
    }
    public static Subject set(Object key, Object value) {
        return new WrapSubject(new HashSubject().set(key, value));
    }
    public static Subject set(Glass<?, ?> glassKey, Object value) {
        return new WrapSubject(new HashSubject().set(glassKey, value));
    }
    public static Subject set(Class<?> classKey, Object value) {
        return new WrapSubject(new HashSubject().set(classKey, value));
    }
    public static Subject set(Coupon<?> coupon, Object value) {
        return new WrapSubject(new HashSubject().set(coupon, value));
    }
    public static Subject set(Object key, Fun fun) {
        return new WrapSubject(new HashSubject().set(key, fun));
    }
    public static Subject set(Object key, Tun tun) {
        return new WrapSubject(new HashSubject().set(key, tun));
    }

    public static Subject forge(Subject source, Object... keys) {
        return new WrapSubject(new HashSubject().forge(source, keys));
    }
    public static Subject forgeExisting(Subject source, Object... keys) {
        return new WrapSubject(new HashSubject().forgeExisting(source, keys));
    }

    public static <C> FlowCollection<C> values(Subject subject) {
        return subject.keys().mapTo(subject::get);
    }

    public static <C> FlowCollection<C> values(Subject subject, Glass<C, C> glassFilter, boolean skipFiltered) {
        return skipFiltered ?
                subject.values().mapTo(v -> glassFilter.isInstance(v) ? glassFilter.cast(v) : null, true) :
                values(subject);
    }

    public static <C> FlowCollection<C> values(Subject subject, Glass<C, C> glassFilter) {
        return values(subject, glassFilter, false);
    }

    public static <C> FlowCollection<C> values(Subject subject, Class<C> classFilter, boolean skipFiltered) {
        return values(subject, Glass.of(classFilter), skipFiltered);
    }

    public static <C> FlowCollection<C> values(Subject subject, Class<C> classFilter) {
        return values(subject, Glass.of(classFilter), false);
    }

    @SuppressWarnings("unchecked")
    public static <C> FlowCollection<C> keys(Subject subject) {
        return subject.keys().mapTo(o -> (C)o);
    }

    public static <C> FlowCollection<C> keys(Subject subject, Glass<C, C> glassFilter, boolean skipFiltered) {
        return skipFiltered ?
                subject.keys().mapTo(v -> glassFilter.isInstance(v) ? glassFilter.cast(v) : null, true) :
                keys(subject);
    }

    public static <C> FlowCollection<C> keys(Subject subject, Glass<C, C> glassFilter) {
        return keys(subject, glassFilter, false);
    }

    public static <C> FlowCollection<C> keys(Subject subject, Class<C> classFilter, boolean skipFiltered) {
        return keys(subject, Glass.of(classFilter), skipFiltered);
    }

    public static <C> FlowCollection<C> keys(Subject subject, Class<C> classFilter) {
        return keys(subject, Glass.of(classFilter), false);
    }

    public static int size(Subject subject) {
        return subject.keys().size();
    }
}
