package app.core.suite;

public class Sub<K, V> /*implements Subjective*/ {
//
//    private final Class<K> keyType;
//    private final Class<V> valueType;
//    private Subject subject = ZeroSubject.getInstance();
//
//    public Sub(Class<K> keyType, Class<V> valueType) {
//        this.keyType = keyType;
//        this.valueType = valueType;
//    }
//
//    public Sub<K, V> set(K key, V value) {
//        subject = subject.set(key, value);
//        return this;
//    }
//
//    public Sub<K, V> put(K key, V value) {
//        subject = subject.put(key, value);
//        return this;
//    }
//
//    public Sub<K, V> unset() {
//        subject = subject.unset();
//        return this;
//    }
//
//    public Sub<K, V> unset(K key) {
//        subject = subject.unset(key);
//        return this;
//    }
//
//    public V prime() {
//        return subject.prime().orSpare(null);
//    }
//
//    public V recent() {
//        return subject.recent().orSpare(null);
//    }
//    public V get() {
//        return subject.get().orSpare(null);
//    }
//
//    public V get(K key) {
//        return subject.get(key).orSpare(null);
//    }
//
//    public K key() {
//        return subject.key().orSpare(null);
//    }
//
//    public K key(K key) {
//        return subject.key(key).orSpare(null);
//    }
//
//    public V getSpared(K key, V reserve) {
//        Subject spared = subject.get(key);
//        if(spared.settled())return spared.asExpected();
//        subject = subject.set(key, reserve);
//        return subject.recent().asExpected();
//    }
//
//    public V getDone(K key, Supplier<V> supplier) {
//        Subject done = subject.get(key);
//        if(done.settled())return done.asExpected();
//        V v = supplier.get();
//        subject = subject.set(key, v);
//        return v;
//    }
//
//    public V take(K key) {
//        Subject taken = subject.get(key);
//        subject = subject.unset(key);
//        return taken.orSpare(null);
//    }
//
//    public boolean settled() {
//        return subject.settled();
//    }
//
//    public int size() {
//        return subject.size();
//    }
//
//    public FlowIterable<V> values(boolean lastFirst) {
//        return subject.values(lastFirst).filter(valueType);
//    }
//
//    public FlowIterable<K> keys(boolean lastFirst) {
//        return subject.keys().filter(keyType);
//    }
//
//    public FlowIterable<V> values() {
//        return values(false);
//    }
//
//    public FlowIterable<K> keys() {
//        return keys(false);
//    }
//
//    public Sub<K, V> insetAll(Iterable<Subject> iterable) {
//        for(Subject it : iterable) {
//            if(it.isIn(valueType) && it.key().isIn(keyType)) {
//                subject = subject.set(it.key().direct(), it.direct());
//            }
//        }
//        return this;
//    }
//
//    public Sub<K, V> inputAll(Iterable<Subject> iterable) {
//        for(Subject it : iterable) {
//            if(it.isIn(valueType) && it.key().isIn(keyType)) {
//                subject = subject.put(it.key().direct(), it.direct());
//            }
//        }
//        return this;
//    }
//
//    @Override
//    public Subject perform() {
//        return subject;
//    }
//
//    @Override
//    public void fromSubject(Subject subject) {
//        inputAll(subject);
//    }
}
