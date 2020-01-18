package app.core.suite;

import app.core.flow.FlowCollection;
import app.core.suite.transition.*;

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
    public static Subject set(Coupon<?> coupon, Object value) {
        return new WrapSubject(new HashSubject().set(coupon, value));
    }
    public static Subject set(Object key, Transition transition) {
        System.out.println("transition");
        return new WrapSubject(new HashSubject().set(key, transition));
    }
    public static Subject set(Object key, Impression impression) {
        System.out.println("impression");
        return new WrapSubject(new HashSubject().set(key, impression));
    }
    public static Subject set(Object key, Statement statement) {
        System.out.println("statement");
        return new WrapSubject(new HashSubject().set(key, statement));
    }
    public static Subject set(Object key, Action action) {
        System.out.println("action");
        return new WrapSubject(new HashSubject().set(key, action));
    }
    public static Subject set(Object key, Function function) {
        System.out.println("function");
        return new WrapSubject(new HashSubject().set(key, function));
    }
    public static Subject set(Object key, Expression expression) {
        System.out.println("expression");
        return new WrapSubject(new HashSubject().set(key, expression));
    }
    public static Subject set(Object key, HazardousTransition transition) {
        System.out.println("hazard transition");
        return new WrapSubject(new HashSubject().set(key, transition));
    }
    public static Subject set(Object key, HazardousImpression impression) {
        System.out.println("hazard impression");
        return new WrapSubject(new HashSubject().set(key, impression));
    }
    public static Subject set(Object key, HazardousAction action) {
        System.out.println("hazard action");
        return new WrapSubject(new HashSubject().set(key, action));
    }
    public static Subject set(Object key, HazardousFunction function) {
        System.out.println("hazard function");
        return new WrapSubject(new HashSubject().set(key, function));
    }
    public static Subject set(Object key, HazardousExpression expression) {
        System.out.println("hazard expression");
        return new WrapSubject(new HashSubject().set(key, expression));
    }

    public static Subject met(Subject source, Object... keys) {
        return new WrapSubject(new HashSubject().met(source, keys));
    }
    public static Subject mix(Subject source, Object... keys) {
        return new WrapSubject(new HashSubject().mix(source, keys));
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
