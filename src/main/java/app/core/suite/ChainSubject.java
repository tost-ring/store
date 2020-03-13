package app.core.suite;

import app.core.flow.Chain;
import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;
import app.core.flow.FlowIterator;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ChainSubject implements Subject {

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
    public <B> Subject sen(Class<B> key) {
        try {
            return set(key, key.getConstructor().newInstance());
        } catch (Exception e) {
            throw new NullPointerException("Failed instance creation of " + key);
        }
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
    @SuppressWarnings("unchecked")
    public<B> B get() {
        Object o = chain.getFirstLink().getValue();
        if(o == null) {
            throw new NullPointerException("Missing first value");
        }
        return (B)o;
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

    public int size() {
        return chain.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K> K getKey() {
        Object o = chain.getFirstLink().getKey();
        if(o == null) {
            throw new NullPointerException("Missing first key");
        }
        return (K)o;
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        Object o = chain.getFirstLink().getKey();
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        Object o = chain.getFirstLink().getKey();
        return requestedType.isInstance(o) ? requestedType.cast(o) : substitute;
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return new FlowIterator<>() {
            Iterator<Map.Entry<Object, Object>> origin = chain.iterator();

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public Subject next() {
                var entry = origin.next();
                return Suite.set(entry.getKey(), entry.getValue());
            }
        };
    }

    public Stream<Subject> stream() {
        Spliterator<Subject> spliterator = new Spliterator<>() {
            Iterator<Subject> iterator = iterator();

            @Override
            public boolean tryAdvance(Consumer<? super Subject> action) {
                if (action == null) throw new NullPointerException();
                if (iterator.hasNext()) {
                    action.accept(iterator.next());
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<Subject> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return size();
            }

            @Override
            public int characteristics() {
                return DISTINCT | NONNULL | ORDERED | SIZED;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public String toString() {
        return "$" + chain;
    }

    @Override
    public int hashCode() {
        return Suite.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subject && Suite.equals(this, (Subject)obj);
    }
}
