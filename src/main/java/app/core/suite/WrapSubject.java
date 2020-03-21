package app.core.suite;

import app.core.flow.FlowIterator;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WrapSubject implements Subject {

    private Subject subject;

    WrapSubject() {
        subject = ZeroSubject.getInstance();
    }

    WrapSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object element) {
        subject = subject.set(element);
        return this;
    }

    @Override
    public Subject set(Object key, Object value) {
        subject = subject.set(key, value);
        return this;
    }

    @Override
    public Subject sos(Object value) {
        subject = subject.sos(value);
        return this;
    }

    @Override
    public Subject sos(Object key, Object value) {
        subject = subject.sos(key, value);
        return this;
    }

    @Override
    public <B> Subject sen(Class<B> key) {
        subject = subject.sen(key);
        return this;
    }

    @Override
    public Subject add(Object element) {
        subject = subject.add(element);
        return this;
    }

    @Override
    public Subject unset() {
        subject = subject.unset();
        return this;
    }

    @Override
    public Subject unset(Object key) {
        subject = subject.unset(key);
        return this;
    }

    @Override
    public <B> B get() {
        return subject.get();
    }

    @Override
    public <B> B get(Object key) {
        return subject.get(key);
    }

    @Override
    public <B> B getAs(Class<B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B getAs(Glass<? super B, B> requestedType) {
        return subject.getAs(requestedType);
    }

    @Override
    public <B> B getAs(Object key, Class<B> requestedType) {
        return subject.getAs(key, requestedType);
    }

    @Override
    public <B> B getAs(Object key, Glass<? super B, B> requestedType) {
        return subject.getAs(key, requestedType);
    }

    @Override
    public <B> B god(B substitute) {
        return subject.god(substitute);
    }

    @Override
    public <B> B god(Object key, B substitute) {
        return subject.god(key, substitute);
    }

    @Override
    public <B> B godAs(B substitute, Class<B> requestedType) {
        return subject.godAs(substitute, requestedType);
    }

    @Override
    public <B> B godAs(B substitute, Glass<? super B, B> requestedType) {
        return subject.godAs(substitute, requestedType);
    }

    @Override
    public <B> B godAs(Object key, B substitute, Class<B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
    }

    @Override
    public <B> B godAs(Object key, B substitute, Glass<? super B, B> requestedType) {
        return subject.godAs(key, substitute, requestedType);
    }

    @Override
    public <B> B gom(Supplier<B> supplier) {
        return subject.gom(supplier);
    }

    @Override
    public <B> B gom(Object key, Supplier<B> supplier) {
        return subject.gom(key, supplier);
    }

    @Override
    public <B> B gs(B substitute) {
        subject = subject.sos(substitute);
        return subject.get();
    }

    @Override
    public<B> B gs(Object key, B substitute) {
        subject = subject.sos(key, substitute);
        return subject.get();
    }

    @Override
    public <B> B gms(Supplier<B> supplier) {
        return gs(gom(supplier));
    }

    @Override
    public <B> B gms(Object key, Supplier<B> supplier) {
        return gs(key, gom(key, supplier));
    }

    @Override
    public <B> B gac(Class<B> key) {
        return subject.gac(key);
    }

    @Override
    public <B> B gon(Class<B> classKey) {
        return subject.gon(classKey);
    }

    @Override
    public boolean is() {
        return subject.is();
    }

    @Override
    public <B> boolean isi(Class<B> checkedType) {
        return subject.isi(checkedType);
    }

    @Override
    public boolean is(Object key) {
        return subject.is(key);
    }

    @Override
    public <B> boolean isi(Object key, Class<B> classFilter) {
        return subject.isi(key, classFilter);
    }

    @Override
    public boolean are(Object... keys) {
        return subject.are(keys);
    }

    public int size() {
        return subject.size();
    }

    @Override
    public <K> K getKey() {
        return subject.getKey();
    }

    @Override
    public <K> K godKey(K substitute, Class<K> requestedType) {
        return subject.godKey(substitute, requestedType);
    }

    @Override
    public <K> K godKey(K substitute, Glass<? super K, K> requestedType) {
        return subject.godKey(substitute, requestedType);
    }

    @Override
    public Subject met(Subject that) {
        subject = subject.met(that);
        return this;
    }

    @Override
    public Subject met(Subject that, Object... keys) {
        subject = subject.met(that, keys);
        return this;
    }

    @Override
    public Subject mix(Subject source, Object... keys) {
        subject = subject.mix(source, keys);
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
