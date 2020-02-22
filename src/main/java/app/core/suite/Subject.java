package app.core.suite;

import app.core.flow.FlowCollection;

import java.util.function.Supplier;

public interface Subject extends Subjective {

    Object toString = new Object();

    Subject set(Object value);
    Subject set(Object key, Object value);
    Subject sos(Object value);
    Subject sos(Object key, Object value);
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
    FlowCollection<Object> keys();
    FlowCollection<Object> values();

    default Subject met(Subject that) {
        Subject subject = this;
        for(Object key : that.keys()) {
            subject = subject.set(key, that.get(key));
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

//    default Subject setFun(Object key, Impression impression) {
//        return setFun(key, (Transition)impression);
//    }
//    default Subject setFun(Object key, Statement statement) {
//        return setFun(key, (Transition)statement);
//    }
//    default Subject setFun(Object key, Action action) {
//        return setFun(key, (Transition)action);
//    }
//    default Subject setFun(Object key, Function function) {
//        return setFun(key, (Transition)function);
//    }
//    default Subject setFun(Object key, Expression expression) {
//        return setFun(key, (Transition)expression);
//    }
//    default Subject setFun(Object key, HazardousTransition hazardousTransition) {
//        return setFun(key, (Transition)hazardousTransition);
//    }
//    default Subject setFun(Object key, HazardousImpression hazardousImpression) {
//        return setFun(key, (Transition)hazardousImpression);
//    }
//    default Subject setFun(Object key, HazardousAction hazardousAction) {
//        return setFun(key, (Transition)hazardousAction);
//    }
//    default Subject setFun(Object key, HazardousFunction hazardousFunction) {
//        return setFun(key, (Transition)hazardousFunction);
//    }
//    default Subject setFun(Object key, HazardousExpression hazardousExpression) {
//        return setFun(key, (Transition)hazardousExpression);
//    }
//
//    default Subject sosFun(Object key, Impression impression) {
//        return sosFun(key, (Transition)impression);
//    }
//    default Subject sosFun(Object key, Statement statement) {
//        return sosFun(key, (Transition)statement);
//    }
//    default Subject sosFun(Object key, Action action) {
//        return sosFun(key, (Transition)action);
//    }
//    default Subject sosFun(Object key, Function function) {
//        return sosFun(key, (Transition)function);
//    }
//    default Subject sosFun(Object key, Expression expression) {
//        return sosFun(key, (Transition)expression);
//    }
//    default Subject sosFun(Object key, HazardousTransition hazardousTransition) {
//        return sosFun(key, (Transition)hazardousTransition);
//    }
//    default Subject sosFun(Object key, HazardousImpression hazardousImpression) {
//        return sosFun(key, (Transition)hazardousImpression);
//    }
//    default Subject sosFun(Object key, HazardousAction hazardousAction) {
//        return sosFun(key, (Transition)hazardousAction);
//    }
//    default Subject sosFun(Object key, HazardousFunction hazardousFunction) {
//        return sosFun(key, (Transition)hazardousFunction);
//    }
//    default Subject sosFun(Object key, HazardousExpression hazardousExpression) {
//        return sosFun(key, (Transition)hazardousExpression);
//    }
}
