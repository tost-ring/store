package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;
import app.core.suite.transition.Action;
import app.core.suite.transition.Transition;

import java.util.Objects;
import java.util.function.Supplier;

public class PrimeSubject implements Subject {

    private Object primeKey;
    private Object primeValue;

    protected Subject upgrade() {
        return new HashSubject().set(primeKey, primeValue);
    }

    protected Object internalGet(Object key) {
        return primeValue != null && Objects.equals(primeKey, key) ? primeValue : null;
    }

    @Override
    public Subject set(Object value) {
        return set(value, value);
    }

    @Override
    public Subject set(Object key, Object value) {
        if(primeValue == null) {
            primeKey = key;
            primeValue = value;
        } else {
            if (Objects.equals(primeKey, key)) {
                throw new NullPointerException("Cant set value for key " + value + ". Key is already associated with " + primeValue);
            } else if(value != null) {
                return upgrade().set(key, value);
            }
        }
        return this;
    }

    @Override
    public Subject set(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            return set((Object)coupon, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
    }

    @Override
    public Subject set(Object key, Transition transition) {
        return set(key, (Object)transition);
    }

    @Override
    public Subject sor(Object value) {
        return sor(value, value);
    }

    @Override
    public Subject sor(Object key, Object value) {
        if(primeValue == null) {
            primeKey = key;
            primeValue = value;
        } else {
            if (Objects.equals(primeKey, key)) {
                primeValue = value;
            } else if(value != null){
                return upgrade().set(key, value);
            }
        }
        return this;
    }

    @Override
    public Subject sor(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            return set((Object)coupon, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
    }

    @Override
    public Subject sor(Object key, Transition transition) {
        return sor(key, (Object)transition);
    }

    @Override
    public Subject sos(Object value) {
        return sos(value, value);
    }

    @Override
    public Subject sos(Object key, Object value) {
        if(primeValue == null) {
            primeKey = key;
            primeValue = value;
        } else {
            if (!Objects.equals(primeKey, key) && value != null) {
                return upgrade().set(key, value);
            }
        }
        return this;
    }

    @Override
    public Subject sos(Coupon<?> coupon, Object value) {
        return sos((Object)coupon, value);
    }

    @Override
    public Subject sos(Object key, Transition transition) {
        return sos(key, (Object)transition);
    }

    @Override
    public Subject unset(Object key) {
        if(Objects.equals(primeKey, key)) {
            primeValue = null;
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B get() {
        return (B)primeValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B get(Object key) {
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
    @SuppressWarnings("unchecked")
    public<B> B god(B substitute) {
        return primeValue != null ? (B)primeValue : substitute;
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
    @SuppressWarnings("unchecked")
    public <B> B gom(Supplier<B> supplier) {
        return primeValue != null ? (B)primeValue : supplier.get();
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
        if(primeValue == null) {
            throw new UnsupportedOperationException("Self upgrade in gos method is not supported for PrimeSubject");
        } else {
            return (B) primeValue;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B goc(Object key, B substitute) {
        if(primeValue == null) {
            primeKey = key;
            primeValue = substitute;
            return substitute;
        } else if(Objects.equals(primeKey, key)){
            return (B) primeValue;
        } else {
            throw new UnsupportedOperationException("Self upgrade in gos method is not supported for PrimeSubject");
        }
    }

    @Override
    public <B> B gac(Class<B> key) {
        return get(key);
    }

    @Override
    public<B> B gon(Class<B> classKey) {
        B b = god(classKey, null);
        if(b == null) {
            try {
                return classKey.getConstructor().newInstance();
            } catch (Exception e) {
                throw new NullPointerException("Failed instance creation of " + classKey);
            }
        } else {
            return b;
        }
    }

    @Override
    public boolean is() {
        return primeValue != null;
    }

    @Override
    public <B> boolean iso(Class<B> checkedType) {
        return checkedType.isInstance(primeValue);
    }

    @Override
    public boolean is(Object key) {
        return internalGet(key) != null;
    }

    @Override
    public <B>boolean iso(Object key, Class<B> classFilter){
        return classFilter.isInstance(internalGet(key));
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
        return primeValue != null ? new FlowArrayList<>(primeKey) : new FlowArrayList<>();
    }

    @Override
    public FlowCollection<Object> values() {
        return primeValue != null ? new FlowArrayList<>(primeValue) : new FlowArrayList<>();
    }

    @Override
    public String toString() {
        if(iso(toString, Action.class)) return act().get();
        else if(primeValue == null) return "Subject{}";
        else return "Subject{" + primeKey + "=" + primeValue + "}";
    }
}
