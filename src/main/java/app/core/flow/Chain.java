package app.core.flow;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Chain<K, V> implements Map<K, V>, FlowIterable<Map.Entry<K, V>> {

    public class Link implements Entry<K, V>{
        private Link front;
        private Link back;
        private K key;
        private Object value;

        public Link(Link front, Link back, K key, Object value) {
            this.front = front;
            this.back = back;
            this.key = key;
            this.value = value;
        }

        Link(Link that){
            this.front = that.front;
            this.back = that.back;
            this.key = that.key;
            this.value = that.value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            return Objects.equals(value, obj);
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        @SuppressWarnings("unchecked")
        public V getValue() {
            return (V)value;
        }

        public Link getFront() {
            return front;
        }

        public Link getBack() {
            return back;
        }

        @Override
        public V setValue(V value) {
            V seniorValue = getValue();
            this.value = value;
            return seniorValue;
        }
    }

    public class ChainIterator implements FlowIterator<Entry<K, V>> {

        private boolean reverse;
        private Link first;
        private Link last;
        private Link current;
        private int expectedModCount = modCount;

        public ChainIterator(Link first, Link last) {
            this(first, last, false);
        }

        public ChainIterator(Link first, Link last, boolean reverse) {
            this.first = first;
            this.last = last;
            this.reverse = reverse;
        }

        public boolean hasNext() {
            return current == null ? first != null : current != last;
        }

        public Link next() {
            checkForComodification();
            current = current == null ? first : reverse ? current.back : current.front;
            return current;
        }

        public void remove() {
            checkForComodification();
            if (current == null)
                throw new IllegalStateException();

            Chain.this.remove(current.getKey());

            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && hasNext()) {
                action.accept(next());
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    public class ChainSpliterator implements Spliterator<Entry<K, V>> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        private final Chain<K, V> chain; // null OK unless traversed
        private Link current;      // current node; null until initialized
        private int est;              // size estimate; -1 until first needed
        private int expectedModCount; // initialized when est set
        private int batch;            // batch size for splits

        ChainSpliterator(Chain<K, V> chain, int est, int expectedModCount) {
            this.chain = chain;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final Chain<K, V> ch;
            if ((s = est) < 0) {
                if ((ch = chain) == null)
                    s = est = 0;
                else {
                    expectedModCount = ch.modCount;
                    current = ch.head;
                    s = est = ch.size();
                }
            }
            return s;
        }

        public long estimateSize() { return (long) getEst(); }

        public Spliterator<Entry<K, V>> trySplit() {
            Link p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do { a[j++] = p; } while ((p = p.front) != chain.head && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED | Spliterator.DISTINCT);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            Link p; int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    Link e = p;
                    p = p.front;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (chain.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super Entry<K, V>> action) {
            Link p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                current = p.front;
                action.accept(p);
                if (chain.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    private Map<K, Link> data;
    private Link head;
    private final Link zeroLink = new Link(null, null, null, null);
    private boolean fifo;
    private int modCount;

    public Chain() {
        this(false);
    }

    public Chain(boolean fifo) {
        data = new HashMap<>();
        this.fifo = fifo;
    }

    public Chain(int initialCapacity, boolean fifo) {
        data = new HashMap<>(initialCapacity);
        this.fifo = fifo;
    }

    public Chain(Chain<K, V> that) {
        this();
        for(Map.Entry<K, Link> it : that.data.entrySet()){
            data.put(it.getKey(), new Link(it.getValue()));
        }
        this.head = that.head;
        this.fifo = that.fifo;
    }

    public boolean isFifo() {
        return fifo;
    }

    public void setFifo(boolean fifo) {
        this.fifo = fifo;
    }

    public void rotateFront() {
        if(head != null)
            head = head.front;
    }

    public void rotateBack() {
        if(head != null)
            head = head.back;
    }

    public boolean rotateAt(Object o) {
        Link link = data.get(o);
        if(link == null)return false;
        head = link;
        return true;
    }

    public Link getLink(Object key) {
        return data.get(key);
    }

    public Link getFirstLink() {
        return head;
    }

    public Link getLastLink() {
        return head == null ? null : head.back;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(new Link(null, null, null, value));
    }

    @Override
    public V get(Object key) {
        return data.getOrDefault(key, zeroLink).getValue();
    }

    @Override
    public V put(K key, V value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        V seniorValue = null;

        if(linkSenior == null) {
            if(head == null) {
                head = linkJunior.front = linkJunior.back = linkJunior;
            } else {
                linkJunior.front = head;
                linkJunior.back = head.back;
                head.back.front = linkJunior;
                head.back = linkJunior;
                if(!fifo) {
                    head = linkJunior;
                }
            }
        } else {
            seniorValue = linkSenior.getValue();
            linkSenior.setValue(value);
            if(linkSenior != head) {
                if(linkSenior != head.back) {
                    linkSenior.front.back = linkSenior.back;
                    linkSenior.back.front = linkSenior.front;
                    linkSenior.front = head;
                    linkSenior.back = head.back;
                    head.back.front = linkSenior;
                    head.back = linkSenior;
                }
                head = linkSenior;
            }
            if(fifo) {
                head = head.front;
            }
        }

        return seniorValue;
    }

    @Override
    public V remove(Object key) {

        Link linkSenior = data.remove(key);
        V seniorValue = null;

        if(linkSenior != null) {
            seniorValue = linkSenior.getValue();
            if(linkSenior == head) {
                if(linkSenior == head.front) {
                    head = null;
                } else {
                    linkSenior.front.back = linkSenior.back;
                    linkSenior.back.front = linkSenior.front;
                    head = linkSenior.front;
                }
            } else {
                linkSenior.front.back = linkSenior.back;
                linkSenior.back.front = linkSenior.front;
            }
        }

        return seniorValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<K> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<V> values() {
        return stream().map(Entry::getValue).collect(Collectors.toUnmodifiableList());
    }

    public FlowIterator<K> keysIterator(boolean reverse) {
        return new FlowIterator<>() {
            Iterator<Entry<K, V>> origin = iterator(reverse);

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public K next() {
                return origin.next().getKey();
            }
        };
    }

    public FlowIterator<V> valuesIterator(boolean reverse) {
        return new FlowIterator<>() {
            Iterator<Entry<K, V>> origin = iterator(reverse);

            @Override
            public boolean hasNext() {
                return origin.hasNext();
            }

            @Override
            public V next() {
                return origin.next().getValue();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(data.values());
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for(Entry<K, V> entry : this) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        for(Entry<K, V> entry : this) {
            V juniorValue = function.apply(entry.getKey(), entry.getValue());
            entry.setValue(juniorValue);
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {

        Link linkJunior = new Link(null, null, key, value);
        Link linkSenior = data.putIfAbsent(key, linkJunior);
        V seniorValue = null;

        if(linkSenior == null) {
            if(head == null) {
                head = linkJunior.front = linkJunior.back = linkJunior;
            } else {
                linkJunior.front = head;
                linkJunior.back = head.back;
                head.back.front = linkJunior;
                head.back = linkJunior;
                head = linkJunior;
            }
        } else {
            seniorValue = linkSenior.getValue();
        }

        return seniorValue;
    }

    @Override
    public boolean remove(Object key, Object value) {

        Link link = data.get(key);
        return link != null && link.equals(value) && data.remove(key) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {

        Link link = data.get(key);
        if(link == null) return false;
        if(link.equals(oldValue)) {
            link.setValue(newValue);
            return true;
        } else return false;
    }

    @Override
    public V replace(K key, V value) {

        Link link = data.get(key);
        return link == null ? null : link.setValue(value);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {

        Link link = data.get(key);
        V valueJunior = null;

        if(link == null || link.getValue() == null) {
            valueJunior = mappingFunction.apply(key);
            if(valueJunior != null) {
                put(key, valueJunior);
            }
        }

        return valueJunior;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {

        Link link = data.get(key);
        V valueJunior = null;

        if (link != null) {
            valueJunior = remappingFunction.apply(key, link.getValue());
            if (valueJunior != null) {
                link.setValue(valueJunior);
            } else {
                remove(key);
            }
        }

        return valueJunior;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {

        Link link = data.get(key);
        V valueJunior = null;

        if(link == null) {
            valueJunior = remappingFunction.apply(key, null);
            if(valueJunior != null) {
                put(key, valueJunior);
            }
        } else {
            valueJunior = remappingFunction.apply(key, link.getValue());
            if(valueJunior != null) {
                link.setValue(valueJunior);
            } else {
                remove(key);
            }
        }

        return valueJunior;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {

        Link link = data.get(key);
        V valueJunior = null;

        if(link == null) {
            if(value != null) {
                valueJunior = value;
                put(key, valueJunior);
            }
        } else {
            if(link.getValue() == null) {
                if(value != null) {
                    valueJunior = value;
                    link.setValue(valueJunior);
                } else {
                    remove(key);
                }
            } else {
                valueJunior = remappingFunction.apply(link.getValue(), value);
                if(value != null) {
                    link.setValue(valueJunior);
                } else {
                    remove(key);
                }
            }
        }

        return valueJunior;
    }

    @Override
    public FlowIterator<Entry<K, V>> iterator() {
        return new ChainIterator(head, head == null ? null : head.back);
    }

    public FlowIterator<Entry<K, V>> iterator(boolean reverse) {
        if(reverse) {
            return new ChainIterator(head == null ? null : head.back, head, true);
        } else {
            return new ChainIterator(head, head == null ? null : head.back);
        }
    }

    @Override
    public void forEach(Consumer<? super Entry<K, V>> action) {
        for(Entry<K, V> it : this) {
            action.accept(it);
        }
    }

    @Override
    public ChainSpliterator spliterator() {
        return new ChainSpliterator(this, -1, 0);
    }

    public Stream<Entry<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<Entry<K, V>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        Iterator<Entry<K, V>> iterator = iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if(iterator.hasNext()){
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
