package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HashSubject implements Subject, Serializable {

    private Map<Object, Object> fields;
    private Object lastRequested = null;

    HashSubject(Map<Object, Object> fields) {
        this.fields = fields;
    }

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
    public Subject set(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            internalSet(classKey, value, SetMode.SET_ELSE_THROW);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
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
    public Subject set(Object key, Fun fun) {
        internalSet(key, fun, SetMode.SET_ELSE_THROW);
        return this;
    }

    @Override
    public Subject set(Object key, Tun tun) {
        internalSet(key, tun, SetMode.SET_ELSE_THROW);
        return this;
    }

    @Override
    public Subject set(Object key, Statement statement) {
        internalSet(key, Fun.make(statement), SetMode.SET_ELSE_THROW);
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
    public Subject sor(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            internalSet(classKey, value, SetMode.SET_OR_REPLACE);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
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
    public Subject sor(Object key, Fun fun) {
        internalSet(key, fun, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sor(Object key, Tun tun) {
        internalSet(key, tun, SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sor(Object key, Statement statement) {
        internalSet(key, Fun.make(statement), SetMode.SET_OR_REPLACE);
        return this;
    }

    @Override
    public Subject sok(Object value) {
        internalSet(value, value, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sok(Object key, Object value) {
        internalSet(key, value, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sok(Class<?> classKey, Object value) {
        if(classKey.isInstance(value)) {
            internalSet(classKey, value, SetMode.SET_OR_KEEP);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + classKey);
        }
        return this;
    }

    @Override
    public Subject sok(Coupon<?> coupon, Object value) {
        if(coupon.getGlass().isInstance(value)) {
            internalSet(coupon, value, SetMode.SET_OR_KEEP);
        } else {
            throw new ClassCastException("Value " + value + " must be instance of " + coupon.getGlass());
        }
        return this;
    }

    @Override
    public Subject sok(Object key, Fun fun) {
        internalSet(key, fun, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sok(Object key, Tun tun) {
        internalSet(key, tun, SetMode.SET_OR_KEEP);
        return this;
    }

    @Override
    public Subject sok(Object key, Statement statement) {
        internalSet(key, Fun.make(statement), SetMode.SET_OR_KEEP);
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
    public<B> B gos(B substitute) {
        Object nullOnPutted = internalSet(lastRequested, substitute, SetMode.SET_OR_KEEP);
        return nullOnPutted == null ? substitute : (B)nullOnPutted;
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B gos(Object key, B substitute) {
        Object nullOnPutted = internalSet(key, substitute, SetMode.SET_OR_KEEP);
        return nullOnPutted == null ? substitute : (B)nullOnPutted;
    }

    @Override
    public<B> B gon(Class<B> classKey) {
        Object b = internalGet(classKey);
        if(b == null) {
            try {
                return classKey.getConstructor().newInstance();
            } catch (Exception e) {
                throw new NullPointerException("Failed instance creation of " + classKey);
            }
        } else {
            return classKey.cast(b);
        }
    }

    @Override
    public boolean is() {
        return is(lastRequested);
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
        return new FlowArrayList<>(fields.keySet());
    }

    @Override
    public FlowCollection<Object> values() {
        return new FlowArrayList<>(fields.values());
    }

    @Override
    public String toString() {
        return is(toString) ? get(toString, Fun.class).play(this).get(String.class) : ("Subject" + fields);
    }
}
