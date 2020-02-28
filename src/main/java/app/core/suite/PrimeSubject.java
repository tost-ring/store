package app.core.suite;

import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;

import java.util.Objects;
import java.util.function.Supplier;

public class PrimeSubject implements Subject {

    private Object primeKey;
    private Object primeValue;

    PrimeSubject(Object prime) {
        this(prime, prime);
    }

    PrimeSubject(Object primeKey, Object primeValue) {
        this.primeKey = primeKey;
        this.primeValue = primeValue;
    }

    protected Subject upgrade() {
        return new ChainSubject().set(primeKey, primeValue);
    }

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        if(Objects.equals(primeKey, key)) {
            primeKey = key;
            primeValue = value;
            return this;
        } else {
            return upgrade().set(key, value);
        }
    }

    @Override
    public Subject sos(Object element) {
        return sos(element, element);
    }

    @Override
    public Subject sos(Object key, Object value) {
        if(Objects.equals(primeKey, key)) {
            return this;
        } else {
            return upgrade().set(key, value);
        }
    }

    @Override
    public Subject unset() {
        return ZeroSubject.getInstance();
    }

    @Override
    public Subject unset(Object key) {
        if(Objects.equals(primeKey, key)) {
            return ZeroSubject.getInstance();
        } else {
            return this;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B get() {
        if(primeValue == null) {
            throw new NullPointerException("Missing value associated with key " + primeKey);
        }
        return (B)primeValue;
    }

    @Override
    public <B> B get(Object key) {
        if(Objects.equals(primeKey, key)) {
            return get();
        } else {
            throw new NullPointerException("Missing value associated with key " + key);
        }
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
    public<B> B god(Object key, B substitute) {
        return Objects.equals(primeKey, key) ? god(substitute) : substitute;
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return requestedType.isInstance(primeValue) ? requestedType.cast(primeValue) : substitute;
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return requestedType.isInstance(primeValue) ? requestedType.cast(primeValue) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return Objects.equals(primeKey, key) ? godAs(substitute, requestedType) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return Objects.equals(primeKey, key) ? godAs(substitute, requestedType) : substitute;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B gom(Supplier<B> supplier) {
        return primeValue != null ? (B)primeValue : supplier.get();
    }

    @Override
    public <B> B gom(Object key, Supplier<B> supplier) {
        return Objects.equals(primeKey, key) ? gom(supplier) : supplier.get();
    }

    @Override
    public <B> B gsg(B substitute) {
        return sos(substitute).get();
    }

    @Override
    public<B> B gsg(Object key, B substitute) {
        if(Objects.equals(primeKey, key)) {
            return gsg(substitute);
        } else {
            throw new UnsupportedOperationException("Self upgrade in goc method is not supported for PrimeSubject");
        }
    }

    @Override
    public <B> B gac(Class<B> key) {
        return get(key);
    }

    @Override
    public<B> B gon(Class<B> classKey) {
        if(Objects.equals(primeKey, classKey) && classKey.isInstance(primeValue)) {
            return classKey.cast(primeValue);
        } else {
            try {
                return classKey.getConstructor().newInstance();
            } catch (Exception e) {
                throw new NullPointerException("Failed instance creation of " + classKey);
            }
        }
    }

    @Override
    public boolean is() {
        return primeValue != null;
    }

    @Override
    public <B> boolean isi(Class<B> checkedType) {
        return checkedType.isInstance(primeValue);
    }

    @Override
    public boolean is(Object key) {
        return Objects.equals(primeKey, key) && primeValue != null;
    }

    @Override
    public <B>boolean isi(Object key, Class<B> classFilter){
        return Objects.equals(primeKey, key) && classFilter.isInstance(primeValue);
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
        return FlowArrayList.melt(primeKey);
    }

    @Override
    public FlowCollection<Object> values() {
        return FlowArrayList.melt(primeValue);
    }

    @Override
    public String toString() {
        return "$[" + primeKey + "=" + primeValue + "]";
    }
}
