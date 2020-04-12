package app.core.suite;

import app.core.flow.FlowIterable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

class ThreadySubject implements Subject {

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


    ThreadySubject() {
        subject = ZeroSubject.getInstance();
    }

    ThreadySubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object element) {
        try(var ignored = writeLock.lock()) {
            subject = subject.set(element);
        }
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        try(var ignored = writeLock.lock()) {
            subject = subject.set(key, value);
        }
        return this;
    }

    @Override
    public Subject put(Object value) {
        try(var ignored = writeLock.lock()) {
            subject = subject.put(value);
        }
        return this;
    }

    @Override
    public Subject put(Object key, Object value) {
        try(var ignored = writeLock.lock()) {
            subject = subject.put(key, value);
        }
        return this;
    }

    @Override
    public Subject add(Object element) {
        try(var ignored = writeLock.lock()) {
            subject = subject.add(element);
        }
        return this;
    }

    @Override
    public Subject unset() {
        try(var ignored = writeLock.lock()) {
            subject = subject.unset();
        }
        return this;
    }

    @Override
    public Subject unset(Object key) {
        try(var ignored = writeLock.lock()) {
            subject = subject.unset(key);
        }
        return this;
    }

    @Override
    public Subject unset(Object key, Object value) {
        try(var ignored = writeLock.lock()) {
            subject = subject.unset(key, value);
        }
        return this;
    }

    @Override
    public Subject prime() {
        Subject prime;
        try(var ignored = readLock.lock()) {
            prime = new WrapSubject(subject.prime());
        }
        return prime;
    }

    @Override
    public Subject recent() {
        Subject recent;
        try(var ignored = readLock.lock()) {
            recent = new WrapSubject(subject.recent());
        }
        return recent;
    }

    @Override
    public Subject get(Object key) {
        Subject get;
        try(var ignored = readLock.lock()) {
            get = new WrapSubject(subject.get(key));
        }
        return get;
    }

    @Override
    public Subject key() {
        Subject key;
        try(var ignored = readLock.lock()) {
            key = new WrapSubject(subject.key());
        }
        return key;
    }

    @Override
    public Object direct() {
        Object direct;
        try(var ignored = readLock.lock()) {
            direct = subject.direct();
        }
        return direct;
    }

    @Override
    public <B> B asExpected() {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.asExpected();
        }
        return b;
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.asGiven(requestedType);
        }
        return b;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.asGiven(requestedType);
        }
        return b;
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.asGiven(requestedType, reserve);
        }
        return b;
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.asGiven(requestedType, reserve);
        }
        return b;
    }

    @Override
    public <B> B orGiven(B reserve) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.orGiven(reserve);
        }
        return b;
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        B b;
        try(var ignored = readLock.lock()) {
            b = subject.orDo(supplier);
        }
        return b;
    }

    @Override
    public boolean isIn(Class<?> type) {
        boolean is;
        try(var ignored = readLock.lock()) {
            is = subject.isIn(type);
        }
        return is;
    }

    @Override
    public Subject getSaved(Object key, Object reserve) {
        Subject spared;
        try(var ignored = writeLock.lock()) {
            spared = subject.get(key);
            if(!spared.settled()) {
                subject = subject.set(key, reserve);
                spared = subject.get(key);
            }
        }
        return new WrapSubject(spared);
    }

    @Override
    public Subject getDone(Object key, Supplier<?> supplier) {
        Subject spared;
        try(var ignored = writeLock.lock()) {
            spared = subject.get(key);
            if(!spared.settled()) {
                subject = subject.set(key, supplier.get());
                spared = subject.get(key);
            }
        }
        return new WrapSubject(spared);
    }

    @Override
    public Subject take(Object key) {
        Subject taken;
        try(var ignored = writeLock.lock()) {
            taken = subject.get(key);
            subject = subject.unset(key);
        }
        return new WrapSubject(taken);
    }

    @Override
    public boolean settled() {
        boolean settled;
        try(var ignored = readLock.lock()) {
            settled = subject.settled();
        }
        return settled;
    }

    @Override
    public int size() {
        int size;
        try(var ignored = readLock.lock()) {
            size = subject.size();
        }
        return size;
    }

    @Override
    public Stream<Subject> stream() {
        return subject.stream().map(WrapSubject::new);
    }

    @Override
    public FlowIterable<Subject> front() {
        return subject.front().map(WrapSubject::new);
    }

    @Override
    public FlowIterable<Subject> reverse() {
        return subject.reverse().map(WrapSubject::new);
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        return subject.values(lastFirst);
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return subject.keys(lastFirst);
    }

    @Override
    public Subject setAll(Iterable<Subject> iterable) {
        try(var ignored = writeLock.lock()) {
            subject = subject.setAll(iterable);
        }
        return this;
    }

    @Override
    public Subject putAll(Iterable<Subject> iterable) {
        try(var ignored = writeLock.lock()) {
            subject = subject.putAll(iterable);
        }
        return this;
    }

    @Override
    public boolean fused() {
        boolean fused;
        try(var ignored = readLock.lock()) {
            fused = subject.fused();
        }
        return fused;
    }

    @Override
    public boolean homogeneous() {
        return true;
    }

    @Override
    public String toString() {
        String string;
        try(var ignored = writeLock.lock()) {
            string = subject.toString();
        }
        return string;
    }
}
