package app.core.suite;

import app.core.flow.FlowCollection;
import app.core.flow.FlowIterable;
import app.core.suite.action.*;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Subject extends Subjective, FlowIterable<Subject> {

    Object toString = new Object();

    Subject set(Object element);
    Subject set(Object key, Object value);
    Subject sos(Object element);
    Subject sos(Object key, Object value);
    <B> Subject sen(Class<B> key);
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
    <B> B gom(Supplier<B> supplier);
    <B> B gom(Object key, Supplier<B> supplier);
    <B> B gsg(B substitute);
    <B> B gsg(Object key, B substitute);
    <B> B gac(Class<B> key);
    <B> B gon(Class<B> key);
    boolean is();
    <B>boolean isi(Class<B> checkedType);
    boolean is(Object key);
    <B>boolean isi(Object key, Class<B> checkedType);
    boolean are(Object ... keys);
    <K> K getKey();
    <K> K godKey(K substitute, Class<K> requestedType);
    <K> K godKey(K substitute, Glass<? super K, K> requestedType);
    Stream<Subject> stream();
    int size();

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

    default Subject fuse() {
        return new FuseSubject(this);
    }

//    default Subject act() {
//        return getAs(Transition.class).play(this, Suite.set());
//    }
//
//    default Subject act(Object key) {
//        return getAs(key, Transition.class).play(this, Suite.set());
//    }
//
//    default Subject act(Object key, Subject input) {
//        return getAs(key, Transition.class).play(this, input);
//    }
//
//    default Subject ace() throws Exception {
//        return getAs(Transition.class).gamble(this, Suite.set());
//    }
//
//    default Subject ace(Object key) throws Exception {
//        return getAs(key, Transition.class).gamble(this, Suite.set());
//    }
//
//    default Subject ace(Object key, Subject input) throws Exception{
//        return getAs(key, Transition.class).gamble(this, input);
//    }
//
//    default Subject aod(Object key, Subject substitute) {
//        Transition transition = godAs(key, null, Transition.class);
//        if(transition == null)return substitute;
//        try {
//            return transition.gamble(this, Suite.set());
//        } catch (Exception e) {
//            return substitute;
//        }
//    }
//
//    default Subject aod(Object key, Subject input, Subject substitute) {
//        Transition transition = godAs(key, null, Transition.class);
//        if(transition == null)return substitute;
//        try {
//            return transition.gamble(this, input);
//        } catch (Exception e) {
//            return substitute;
//        }
//    }

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

    default Subject sosFun(Object key, Function function) {
        return sos(key, function);
    }
    default Subject sosFun(Object key, Impression impression) {
        return sos(key, impression);
    }
    default Subject sosFun(Object key, Expression expression) {
        return sos(key, expression);
    }
    default Subject sosFun(Object key, Statement statement) {
        return sos(key, statement);
    }
    default Subject sosHun(Object key, HazardousFunction hazardousFunction) {
        return sos(key, hazardousFunction);
    }
    default Subject sosHun(Object key, HazardousImpression hazardousImpression) {
        return sos(key, hazardousImpression);
    }
    default Subject sosHun(Object key, HazardousExpression hazardousExpression) {
        return sos(key, hazardousExpression);
    }
}
