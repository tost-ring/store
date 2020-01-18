package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;
import app.core.suite.transition.Transition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HashSubject implements Subject {

    enum SetMode {SET_ELSE_THROW, SET_OR_REPLACE, SET_OR_KEEP}
    private Map<Object, Object> fields;
    private Object lastRequested = null;

    public HashSubject() {
        this.fields = new HashMap<>();
    }

    protected Object internalSet(Object key, Object value, SetMode mode) {
        lastRequested = key;
        Object object = null;
        switch (mode) {
            case SET_ELSE_THROW:
                if(value == null) object = fields.get(key);
                else object = fields.putIfAbsent(key, value);
                if(object != null) {
                    throw new NullPointerException("Cant set value for key " + key + ". Key is already associated with " + object);
                }
                break;
            case SET_OR_REPLACE:
                if(value == null) object = fields.remove(key);
                else object = fields.put(key, value);
                break;
            case SET_OR_KEEP:
                if(value == null) object = fields.get(key);
                else object = fields.putIfAbsent(key, value);
                break;
        }
        return object;
    }

    protected Object internalGet(Object key) {
        lastRequested = key;
        return fields.get(key);
    }

    @Override
    public Subject set(Object value) {
        internalSet(value, value, SetMode.SET_ELSE_THROW);
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        internalSet(key, value, SetMode.SET_ELSE_THROW);
        return this;
    }

    @Override
    public Subject set(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            internalSet(coupon, value, SetMode.SET_ELSE_THROW);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
        return this;
    }

    @Override
    public Subject set(Object key, Transition transition) {
        System.out.println("hs set transition");
        internalSet(key, transition, SetMode.SET_ELSE_THROW);
        return this;
    }

    @Override
    public Subject sor(Object value) {
        internalSet(value, value, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sor(Object key, Object value) {
        internalSet(key, value, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sor(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            internalSet(coupon, value, SetMode.SET_OR_REPLACE);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
        return this;
    }

    @Override
    public Subject sor(Object key, Transition transition) {
        internalSet(key, transition, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sos(Object value) {
        internalSet(value, value, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sos(Object key, Object value) {
        internalSet(key, value, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sos(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            internalSet(coupon, value, SetMode.SET_OR_KEEP);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
        return this;
    }

    @Override
    public Subject sos(Object key, Transition transition) {
        internalSet(key, transition, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject unset(Object key) {
        internalSet(key, null, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public<B> B get() {
        return get(lastRequested);
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B get(Object key) {
        Object o = internalGet(key);
        if(o == null) {
            throw new NullPointerException("Missing value associated with key " + key);
        }
        return (B)o;
    }

    @Override
    public<B> B getAs(Class<B> requestedType) {
        return get();
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        return get();
    }

    @Override
    public<B> B get(Coupon<B> coupon) {
        return get((Object)coupon);
    }

    @Override
    public<B> B getAs(Object key, Class<B> requestedType) {
        return get(key);
    }

    @Override
    public<B> B getAs(Object key, Glass<? super B, B> requestedType) {
        return get(key);
    }

    @Override
    public<B> B god(B substitute) {
        return god(lastRequested, substitute);
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B god(Object key, B substitute) {
        Object o = internalGet(key);
        return o != null ? (B)o : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Class<B> requestedType) {
        Object o = internalGet(key);
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        Object o = internalGet(key);
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        return gom(lastRequested, supplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B gom(Object key, Supplier<B> supplier) {
        Object o = internalGet(key);
        return o != null ? (B)o : supplier.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B goc(B substitute) {
        Object nullOnPutted = internalSet(lastRequested, substitute, SetMode.SET_OR_KEEP);
        return nullOnPutted == null ? substitute : (B)nullOnPutted;
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B goc(Object key, B substitute) {
        Object nullOnPutted = internalSet(key, substitute, SetMode.SET_OR_KEEP);
        return nullOnPutted == null ? substitute : (B)nullOnPutted;
    }

    @Override
    public <B> B gac(Class<B> key) {
        return get(key);
    }

    @Override
    public<B> B gon(Class<B> key) {
        Object b = internalGet(key);
        if(b == null) {
            try {
                return key.getConstructor().newInstance();
            } catch (Exception e) {
                throw new NullPointerException("Failed instance creation of " + key);
            }
        } else {
            return key.cast(b);
        }
    }

    @Override
    public boolean is() {
        return is(lastRequested);
    }

    @Override
    public <B> boolean iso(Class<B> checkedType) {
        return checkedType.isInstance(lastRequested);
    }

    @Override
    public boolean is(Object key) {
        return internalGet(key) != null;
    }

    @Override
    public <B>boolean iso(Object key, Class<B> checkedType){
        return checkedType.isInstance(internalGet(key));
    }

    @Override
    public boolean are(Object ... keys) {
        for(Object key : keys) {
            if(!is(key))return false;
        }
        return true;
    }

    @Override
    public FlowCollection<Object> keys() {
        return new FlowArrayList<>(fields.keySet());
    }

    @Override
    public FlowCollection<Object> values() {
        return new FlowArrayList<>(fields.values());
    }

    @Override
    public String toString() {
        return iso(toString, Transition.class) ? act().get() : ("Subject" + fields);
    }
}
