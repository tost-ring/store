package app.core.suite;

import app.core.flow.FlowCollection;
import app.core.suite.transition.*;

import java.util.function.Supplier;

public interface Subject extends Subjective {

    Object toString = new Object();

    Subject set(Object value);
    Subject set(Object key, Object value);
    Subject set(Coupon<?> coupon, Object value);
    Subject set(Object key, Transition transition);
    Subject sor(Object value);
    Subject sor(Object key, Object value);
    Subject sor(Coupon<?> coupon, Object value);
    Subject sor(Object key, Transition transition);
    Subject sos(Object value);
    Subject sos(Object key, Object value);
    Subject sos(Coupon<?> coupon, Object value);
    Subject sos(Object key, Transition transition);
    Subject unset(Object key);
    <B> B get();
    <B> B get(Object key);
    <B> B getAs(Class<B> requestedType);
    <B> B getAs(Glass<? super B, B> requestedType);
    <B> B get(Coupon<B> coupon);
    <B> B getAs(Object key, Class<B> requestedType);
    <B> B getAs(Object key, Glass<? super B, B> requestedType);
    <B> B god(B substitute);
    <B> B god(Object key, B substitute);
    <B> B godAs(Object key, B substitute, Class<B> requestedType);
    <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType);
    <B> B gom(Supplier<B> supplier);
    <B> B gom(Object key, Supplier<B> supplier);
    <B> B goc(B substitute);
    <B> B goc(Object key, B substitute);
    <B> B gac(Class<B> key);
    <B> B gon(Class<B> key);
    boolean is();
    <B>boolean iso(Class<B> checkedType);
    boolean is(Object key);
    <B>boolean iso(Object key, Class<B> checkedType);
    boolean are(Object ... keys);
    FlowCollection<Object> keys();
    FlowCollection<Object> values();

    default Subject met(Subject that) {
        Subject subject = this;
        for(Object key : that.keys()) {
            subject = subject.sor(key, (Object)that.get(key));
        }
        return subject;
    }

    default Subject met(Subject that, Object... keys) {
        Subject subject = this;
        for (Object key : keys) {
            subject = subject.sor(key, that.getAs(key, Object.class));
        }
        return subject;
    }

    default Subject mix(Subject source, Object... keys) {
        Subject subject = this;
        for(Object key : keys) {
            Object o = source.god(key, null);
            if(o != null) {
                subject = subject.sor(key, o);
            }
        }
        return subject;
    }

    default Subject act() {
        return getAs(Transition.class).play(this, Suite.set());
    }

    default Subject act(Object key) {
        return getAs(key, Transition.class).play(this, Suite.set());
    }

    default Subject act(Object key, Subject input) {
        return getAs(key, Transition.class).play(this, input);
    }

    default Subject ace() throws Exception {
        return getAs(Transition.class).gamble(this, Suite.set());
    }

    default Subject ace(Object key) throws Exception {
        return getAs(key, Transition.class).gamble(this, Suite.set());
    }

    default Subject ace(Object key, Subject input) throws Exception{
        return getAs(key, Transition.class).gamble(this, input);
    }

    default Subject aod(Object key, Subject substitute) {
        Transition transition = godAs(key, null, Transition.class);
        if(transition == null)return substitute;
        try {
            return transition.gamble(this, Suite.set());
        } catch (Exception e) {
            return substitute;
        }
    }

    @Override
    default Subject toSubject() {
        return this;
    }

    @Override
    default Subject fromSubject(Subject subject) {
        met(subject);
        return Suite.set(true);
    }

    // Lambdass

    default Subject set(Object key, Impression impression) {
        return set(key, (Transition)impression);
    }
    default Subject set(Object key, Statement statement) {
        return set(key, (Transition)statement);
    }
    default Subject set(Object key, Action action) {
        return set(key, (Transition)action);
    }
    default Subject set(Object key, Function function) {
        return set(key, (Transition)function);
    }
    default Subject set(Object key, Expression expression) {
        return set(key, (Transition)expression);
    }
    default Subject set(Object key, HazardousTransition hazardousTransition) {
        return set(key, (Transition)hazardousTransition);
    }
    default Subject set(Object key, HazardousImpression hazardousImpression) {
        return set(key, (Transition)hazardousImpression);
    }
    default Subject set(Object key, HazardousAction hazardousAction) {
        return set(key, (Transition)hazardousAction);
    }
    default Subject set(Object key, HazardousFunction hazardousFunction) {
        return set(key, (Transition)hazardousFunction);
    }
    default Subject set(Object key, HazardousExpression hazardousExpression) {
        return set(key, (Transition)hazardousExpression);
    }

    default Subject sor(Object key, Impression impression) {
        return sor(key, (Transition)impression);
    }
    default Subject sor(Object key, Statement statement) {
        return sor(key, (Transition)statement);
    }
    default Subject sor(Object key, Action action) {
        return sor(key, (Transition)action);
    }
    default Subject sor(Object key, Function function) {
        return sor(key, (Transition)function);
    }
    default Subject sor(Object key, Expression expression) {
        return sor(key, (Transition)expression);
    }
    default Subject sor(Object key, HazardousTransition hazardousTransition) {
        return sor(key, (Transition)hazardousTransition);
    }
    default Subject sor(Object key, HazardousImpression hazardousImpression) {
        return sor(key, (Transition)hazardousImpression);
    }
    default Subject sor(Object key, HazardousAction hazardousAction) {
        return sor(key, (Transition)hazardousAction);
    }
    default Subject sor(Object key, HazardousFunction hazardousFunction) {
        return sor(key, (Transition)hazardousFunction);
    }
    default Subject sor(Object key, HazardousExpression hazardousExpression) {
        return sor(key, (Transition)hazardousExpression);
    }

    default Subject sos(Object key, Impression impression) {
        return sos(key, (Transition)impression);
    }
    default Subject sos(Object key, Statement statement) {
        return sos(key, (Transition)statement);
    }
    default Subject sos(Object key, Action action) {
        return sos(key, (Transition)action);
    }
    default Subject sos(Object key, Function function) {
        return sos(key, (Transition)function);
    }
    default Subject sos(Object key, Expression expression) {
        return sos(key, (Transition)expression);
    }
    default Subject sos(Object key, HazardousTransition hazardousTransition) {
        return sos(key, (Transition)hazardousTransition);
    }
    default Subject sos(Object key, HazardousImpression hazardousImpression) {
        return sos(key, (Transition)hazardousImpression);
    }
    default Subject sos(Object key, HazardousAction hazardousAction) {
        return sos(key, (Transition)hazardousAction);
    }
    default Subject sos(Object key, HazardousFunction hazardousFunction) {
        return sos(key, (Transition)hazardousFunction);
    }
    default Subject sos(Object key, HazardousExpression hazardousExpression) {
        return sos(key, (Transition)hazardousExpression);
    }
}
