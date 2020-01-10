package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;

import java.util.Objects;
import java.util.function.Supplier;

public class PrimeSubject implements Subject {

    private Object primeKey;
    private Object primeValue;

    public PrimeSubject() {
    }

    public Object getPrimeKey() {
        return primeKey;
    }

    public Object getPrimeValue() {
        return primeValue;
    }

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
    public Subject set(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            return set((Object)classKey, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
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
    public Subject set(Object key, Fun fun) {
        return set(key, (Object)fun);
    }

    @Override
    public Subject set(Object key, Tun tun) {
        return set(key, (Object)tun);
    }

    @Override
    public Subject set(Object key, Statement statement) {
        return set(key, (Object)statement);
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
    public Subject sor(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            return sor((Object)classKey, value);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
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
    public Subject sor(Object key, Fun fun) {
        return sor(key, (Object)fun);
    }

    @Override
    public Subject sor(Object key, Tun tun) {
        return sor(key, (Object)tun);
    }

    @Override
    public Subject sor(Object key, Statement statement) {
        return sor(key, (Object)statement);
    }

    @Override
    public Subject sok(Object value) {
        return sok(value, value);
    }

    @Override
    public Subject sok(Object key, Object value) {
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
    public Subject sok(Class<?> classKey, Object value) {
        return sok((Object)classKey, value);
    }

    @Override
    public Subject sok(Coupon<?> coupon, Object value) {
        return sok((Object)coupon, value);
    }

    @Override
    public Subject sok(Object key, Fun fun) {
        return sok(key, (Object)fun);
    }

    @Override
    public Subject sok(Object key, Tun tun) {
        return sok(key, (Object)tun);
    }

    @Override
    public Subject sok(Object key, Statement statement) {
        return sok(key, (Object)statement);
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
    public<B> B get(Class<? super B> classKey) {
        return get((Object)classKey);
    }

    @Override
    public<B> B get(Coupon<B> coupon) {
        return get((Object)coupon);
    }

    @Override
    public<B> B get(Object key, Class<B> classFilter) {
        return get(key);
    }

    @Override
    public<B> B get(Object key, Glass<? super B, B> glassFilter) {
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
    public<B> B god(Object key, Class<B> classFilter, B substitute) {
        Object o = internalGet(key);
        return classFilter.isInstance(o) ? classFilter.cast(o) : substitute;
    }

    @Override
    public<B> B god(Object key, Glass<? super B, B> glassFilter, B substitute) {
        Object o = internalGet(key);
        return glassFilter.isInstance(o) ? glassFilter.cast(o) : substitute;
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
    public<B> B gos(B substitute) {
        if(primeValue == null) {
            throw new UnsupportedOperationException("Self upgrade in gos method is not supported for PrimeSubject");
        } else {
            return (B) primeValue;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B gos(Object key, B substitute) {
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
    public boolean is(Object key) {
        return internalGet(key) != null;
    }

    @Override
    public <B>boolean is(Object key, Class<B> classFilter){
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
        if(primeValue == null) return "Subject{}";
        else return "Subject{" + primeKey + "=" + primeValue + "}";
    }
}
