package app.core.suite;

import app.core.flow.FlowCollection;

public final class Suite {

    public static Subject set() {
        return new WrapSubject();
    }
    public static Subject set(Object value) {
        return new WrapSubject(new PrimeSubject(value));
    }
    public static Subject set(Object key, Object value) {
        return new WrapSubject(new ChainSubject().set(key, value));
    }

    public static Subject met(Subject source, Object... keys) {
        return new WrapSubject(new ChainSubject().met(source, keys));
    }
    public static Subject mix(Subject source, Object... keys) {
        return new WrapSubject(new ChainSubject().mix(source, keys));
    }

    public static Subject ok() {
        return new WrapSubject(new PrimeSubject("ok"));
    }

    public static Subject error(Object cause) {
        return new WrapSubject(new PrimeSubject("error", cause));
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

//    public static Subject setFun(Object key, Transition transition) {
//        return new WrapSubject(new ChainSubject().setFun(key, transition));
//    }
//    public static Subject setFun(Object key, Impression impression) {
//        return new WrapSubject(new ChainSubject().setFun(key, impression));
//    }
//    public static Subject setFun(Object key, Statement statement) {
//        return new WrapSubject(new ChainSubject().setFun(key, statement));
//    }
//    public static Subject setFun(Object key, Action action) {
//        return new WrapSubject(new ChainSubject().setFun(key, action));
//    }
//    public static Subject setFun(Object key, Function function) {
//        return new WrapSubject(new ChainSubject().setFun(key, function));
//    }
//    public static Subject setFun(Object key, Expression expression) {
//        return new WrapSubject(new ChainSubject().setFun(key, expression));
//    }
//    public static Subject setFun(Object key, HazardousTransition transition) {
//        return new WrapSubject(new ChainSubject().setFun(key, transition));
//    }
//    public static Subject setFun(Object key, HazardousImpression impression) {
//        return new WrapSubject(new ChainSubject().setFun(key, impression));
//    }
//    public static Subject setFun(Object key, HazardousAction action) {
//        return new WrapSubject(new ChainSubject().setFun(key, action));
//    }
//    public static Subject setFun(Object key, HazardousFunction function) {
//        return new WrapSubject(new ChainSubject().setFun(key, function));
//    }
//    public static Subject setFun(Object key, HazardousExpression expression) {
//        return new WrapSubject(new ChainSubject().setFun(key, expression));
//    }
}
