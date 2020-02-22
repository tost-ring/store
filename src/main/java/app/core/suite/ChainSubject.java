package app.core.suite;

import app.core.flow.Chain;
import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;

import java.util.function.Supplier;

public class ChainSubject implements Subject {

    private Chain<Object, Object> chain;

    ChainSubject() {
        this.chain = new Chain<>(true);
    }

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject set(Object key, Object value) {
        chain.put(key, value);
        return this;
    }

    @Override
    public Subject sos(Object element) {
        return sos(element, element);
    }

    @Override
    public Subject sos(Object key, Object value) {
        Object home = chain.get(key);
        if(home == null) {
            chain.put(key, value);
        } else {
            chain.put(key, home);
        }
        return this;
    }

    @Override
    public Subject unset() {
        return unset(chain.getFirstLink().getKey());
    }

    @Override
    public Subject unset(Object key) {
        chain.remove(key);
        return this;
    }

    @Override
    public<B> B get() {
        return get(chain.getFirstLink().getKey());
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B get(Object key) {
        Object o = chain.get(key);
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
    public<B> B getAs(Object key, Class<B> requestedType) {
        return get(key);
    }

    @Override
    public<B> B getAs(Object key, Glass<? super B, B> requestedType) {
        return get(key);
    }

    @Override
    public<B> B god(B substitute) {
        return god(chain.getFirstLink().getKey(), substitute);
    }

    @Override
    @SuppressWarnings("unchecked")
    public<B> B god(Object key, B substitute) {
        Object o = chain.get(key);
        return o != null ? (B)o : substitute;
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return godAs(chain.getFirstLink().getKey(), substitute, requestedType);
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return godAs(chain.getFirstLink().getKey(), substitute, requestedType);
    }

    @Override
    public<B> B godAs(Object key, B substitute, Class<B> requestedType) {
        Object o = chain.get(key);
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public<B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        Object o = chain.get(key);
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        return gom(chain.getFirstLink().getKey(), supplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> B gom(Object key, Supplier<B> supplier) {
        Object o = chain.get(key);
        return o != null ? (B)o : supplier.get();
    }

    @Override
    public <B> B gsg(B substitute) {
        return gsg(chain.getFirstLink().getKey(), substitute);
    }

    @Override
    public<B> B gsg(Object key, B substitute) {
        return sos(key, substitute).get(key);
    }

    @Override
    public <B> B gac(Class<B> key) {
        return get(key);
    }

    @Override
    public<B> B gon(Class<B> key) {
        Object o = chain.get(key);
        if(o == null) {
            try {
                return key.getConstructor().newInstance();
            } catch (Exception e) {
                throw new NullPointerException("Failed instance creation of " + key);
            }
        } else {
            return key.cast(o);
        }
    }

    @Override
    public boolean is() {
        return is(null);
    }

    @Override
    public <B> boolean isi(Class<B> checkedType) {
        return checkedType.isInstance(god(null));
    }

    @Override
    public boolean is(Object key) {
        return chain.get(key) != null;
    }

    @Override
    public <B>boolean isi(Object key, Class<B> checkedType){
        return checkedType.isInstance(god(key, null));
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
        return new FlowArrayList<>(chain.keySet());
    }

    @Override
    public FlowCollection<Object> values() {
        return new FlowArrayList<>(chain.values());
    }

    @Override
    public String toString() {
        return "Subject" + chain;
    }
}
