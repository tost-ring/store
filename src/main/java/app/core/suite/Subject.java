package app.core.suite;

import app.core.flow.FlowIterable;
import app.core.flow.FlowIterator;
import app.core.suite.action.*;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Subject extends Subjective, FlowIterable<Subject> {

    Subject set(Object element);
    Subject set(Object key, Object value);
    Subject sit(Object element);
    Subject sit(Object key, Object value);
    <B> Subject setNew(Class<B> key);
    Subject add(Object element);
    Subject unset();
    Subject unset(Object key);
    <B> B get();
    <B> B get(Object key);
    <B> B getAs(Class<B> requestedType);
    <B> B getAs(Glass<? super B, B> requestedType);
    <B> B getAs(Object key, Class<B> requestedType);
    <B> B getAs(Object key, Glass<? super B, B> requestedType);
    <B> B god(B substitute);
    <B> B god(Object key, B substitute);
    <B> B godAs(B substitute, Class<B> requestedType);
    <B> B godAs(B substitute, Glass<? super B, B> requestedType);
    <B> B godAs(Object key, B substitute, Class<B> requestedType);
    <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType);
    <B> B goMake(Supplier<B> supplier);
    <B> B goMake(Object key, Supplier<B> supplier);
    <B> B getAsGiven(Class<B> key);
    <B> B goNew(Class<B> key);
    boolean is();
    boolean isAsStated(Class<?> checkedType);
    boolean is(Object key);
    boolean isAsStated(Object key, Class<?> checkedType);
    boolean are(Object ... keys);
    <K> K getKey();
    <K> K godKey(K substitute, Class<K> requestedType);
    <K> K godKey(K substitute, Glass<? super K, K> requestedType);
    Stream<Subject> stream();
    FlowIterable<Object> keys(boolean lastFirst);
    FlowIterable<Object> values(boolean lastFirst);
    FlowIterable<Subject> reverse();
    int size();

    default FlowIterable<Object> keys() {
        return keys(false);
    }

    default FlowIterable<Object> values() {
        return values(false);
    }

    default <B> B gs(B substitute) {
        throw new UnsupportedOperationException("Method only supported in homogeneous subjects");
    }
    default <B> B gs(Object key, B substitute) {
        throw new UnsupportedOperationException("Method only supported in homogeneous subjects");
    }
    default <B> B gms(Supplier<B> supplier) {
        throw new UnsupportedOperationException("Method only supported in homogeneous subjects");
    }
    default <B> B gms(Object key, Supplier<B> supplier) {
        throw new UnsupportedOperationException("Method only supported in homogeneous subjects");
    }

    default Subject met(Subject that) {
        Subject subject = this;
        for(Subject it : that) {
            subject = subject.set(it.getKey(), it.get());
        }
        return subject;
    }

    default Subject met(Subject that, Object... keys) {
        Subject subject = this;
        for (Object key : keys) {
            subject = subject.set(key, that.get(key));
        }
        return subject;
    }

    default Subject mix(Subject source, Object... keys) {
        Subject subject = this;
        for(Object key : keys) {
            Object o = source.god(key, null);
            if(o != null) {
                subject = subject.set(key, o);
            }
        }
        return subject;
    }

    @Override
    default Subject toSubject() {
        return this;
    }

    @Override
    default void fromSubject(Subject subject) {
        met(subject);
    }


    // Lambdass

    default Subject setFun(Object key, Function function) {
        return set(key, function);
    }
    default Subject setFun(Object key, Impression impression) {
        return set(key, impression);
    }
    default Subject setFun(Object key, Expression expression) {
        return set(key, expression);
    }
    default Subject setFun(Object key, Statement statement) {
        return set(key, statement);
    }
    default Subject setHun(Object key, HazardousFunction hazardousFunction) {
        return set(key, hazardousFunction);
    }
    default Subject setHun(Object key, HazardousImpression hazardousImpression) {
        return set(key, hazardousImpression);
    }
    default Subject setHun(Object key, HazardousExpression hazardousExpression) {
        return set(key, hazardousExpression);
    }

    default Subject sitFun(Object key, Function function) {
        return sit(key, function);
    }
    default Subject sitFun(Object key, Impression impression) {
        return sit(key, impression);
    }
    default Subject sitFun(Object key, Expression expression) {
        return sit(key, expression);
    }
    default Subject sitFun(Object key, Statement statement) {
        return sit(key, statement);
    }
    default Subject sitHun(Object key, HazardousFunction hazardousFunction) {
        return sit(key, hazardousFunction);
    }
    default Subject sitHun(Object key, HazardousImpression hazardousImpression) {
        return sit(key, hazardousImpression);
    }
    default Subject sitHun(Object key, HazardousExpression hazardousExpression) {
        return sit(key, hazardousExpression);
    }
}
