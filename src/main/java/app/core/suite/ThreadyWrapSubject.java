package app.core.suite;

import app.core.flow.FlowIterator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

class ThreadyWrapSubject implements Subject {

    private static class ThreadyLock implements AutoCloseable {
        private Lock lock;

        public ThreadyLock(Lock lock) {
            this.lock = lock;
        }

        public final ThreadyLock lock() {
            lock.lock();
            return this;
        }

        @Override
        public void close() {
            lock.unlock();
        }
    }

    private Subject subject;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ThreadyLock writeLock = new ThreadyLock(lock.writeLock());
    private final ThreadyLock readLock = new ThreadyLock(lock.readLock());


    ThreadyWrapSubject() {
        subject = ZeroSubject.getInstance();
    }

    ThreadyWrapSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object element) {
        try(var lock = writeLock.lock()) {
            subject = subject.set(element);
        }
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        try(var lock = writeLock.lock()) {
            subject = subject.set(key, value);
        }
        return this;
    }

    @Override
    public Subject sos(Object value) {
        try(var lock = writeLock.lock()) {
            subject = subject.sos(value);
        }
        return this;
    }

    @Override
    public Subject sos(Object key, Object value) {
        try(var lock = writeLock.lock()) {
            subject = subject.sos(key, value);
        }
        return this;
    }

    @Override
    public <B> Subject sen(Class<B> key) {
        try(var lock = writeLock.lock()) {
            subject = subject.sen(key);
        }
        return this;
    }

    @Override
    public Subject add(Object element) {
        try(var lock = writeLock.lock()) {
            subject = subject.add(element);
        }
        return this;
    }

    @Override
    public Subject unset() {
        try(var lock = writeLock.lock()) {
            subject = subject.unset();
        }
        return this;
    }

    @Override
    public Subject unset(Object key) {
        try(var lock = writeLock.lock()) {
            subject = subject.unset(key);
        }
        return this;
    }

    @Override
    public <B> B get() {
        try(var lock = readLock.lock()) {
            return subject.get();
        }
    }

    @Override
    public <B> B get(Object key) {
        try(var lock = readLock.lock()) {
            return subject.get(key);
        }
    }

    @Override
    public <B> B getAs(Class<B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.getAs(requestedType);
        }
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.getAs(requestedType);
        }
    }

    @Override
    public <B> B getAs(Object key, Class<B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.getAs(key, requestedType);
        }
    }

    @Override
    public <B> B getAs(Object key, Glass<? super B, B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.getAs(key, requestedType);
        }
    }

    @Override
    public <B> B god(B substitute) {
        try(var lock = readLock.lock()) {
            return subject.god(substitute);
        }
    }

    @Override
    public <B> B god(Object key, B substitute) {
        try(var lock = readLock.lock()) {
            return subject.god(key, substitute);
        }
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godAs(substitute, requestedType);
        }
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godAs(substitute, requestedType);
        }
    }

    @Override
    public <B> B godAs(Object key, B substitute, Class<B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godAs(key, substitute, requestedType);
        }
    }

    @Override
    public <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godAs(key, substitute, requestedType);
        }
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        try(var lock = readLock.lock()) {
            return subject.gom(supplier);
        }
    }

    @Override
    public <B> B gom(Object key, Supplier<B> supplier) {
        try(var lock = readLock.lock()) {
            return subject.gom(key, supplier);
        }
    }

    @Override
    public <B> B gs(B substitute) {
        try(var lock = writeLock.lock()) {
            subject = subject.sos(substitute);
            return subject.get();
        }
    }

    @Override
    public<B> B gs(Object key, B substitute) {
        try(var lock = writeLock.lock()) {
            subject = subject.sos(key, substitute);
            return subject.get();
        }
    }

    @Override
    public <B> B gms(Supplier<B> supplier) {
        try(var lock = writeLock.lock()) {
            return gs(gom(supplier));
        }
    }

    @Override
    public <B> B gms(Object key, Supplier<B> supplier) {
        try(var lock = writeLock.lock()) {
            return gs(gom(key, supplier));
        }
    }

    @Override
    public <B> B gac(Class<B> key) {
        try(var lock = readLock.lock()) {
            return subject.gac(key);
        }
    }

    @Override
    public <B> B gon(Class<B> classKey) {
        try(var lock = readLock.lock()) {
            return subject.gon(classKey);
        }
    }

    @Override
    public boolean is() {
        try(var lock = readLock.lock()) {
            return subject.is();
        }
    }

    @Override
    public <B> boolean isi(Class<B> checkedType) {
        try(var lock = readLock.lock()) {
            return subject.isi(checkedType);
        }
    }

    @Override
    public boolean is(Object key) {
        try(var lock = readLock.lock()) {
            return subject.is(key);
        }
    }

    @Override
    public <B> boolean isi(Object key, Class<B> classFilter) {
        try(var lock = readLock.lock()) {
            return subject.isi(key, classFilter);
        }
    }

    @Override
    public boolean are(Object... keys) {
        try(var lock = readLock.lock()) {
            return subject.are(keys);
        }
    }

    public int size() {
        try(var lock = readLock.lock()) {
            return subject.size();
        }
    }

    @Override
    public <K> K getKey() {
        try(var lock = readLock.lock()) {
            return subject.getKey();
        }
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godKey(substitute, requestedType);
        }
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        try(var lock = readLock.lock()) {
            return subject.godKey(substitute, requestedType);
        }
    }

    @Override
    public Subject met(Subject that) {
        try(var lock = writeLock.lock()) {
            subject = subject.met(that);
        }
        return this;
    }

    @Override
    public Subject met(Subject that, Object... keys) {
        try(var lock = writeLock.lock()) {
            subject = subject.met(that, keys);
        }
        return this;
    }

    @Override
    public Subject mix(Subject source, Object... keys) {
        try(var lock = writeLock.lock()) {
            subject = subject.mix(source, keys);
        }
        return this;
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return subject.iterator();
    }

    public Stream<Subject> stream() {
        return subject.stream();
    }

    @Override
    public Subject fuse() {
        subject = subject.fuse();
        return this;
    }

    @Override
    public String toString() {
        return subject.toString();
    }
}
