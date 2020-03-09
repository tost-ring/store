package app.core.suite;

import app.core.flow.FlowIterator;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FuseSubject implements Subject {

    private Subject subject;

    FuseSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Subject set(Object element) {
        return Suite.met(subject).set(element);
    }

    @Override
    public Subject set(Object key, Object value) {
        return Suite.met(subject).set(key, value);
    }

    @Override
    public Subject sos(Object value) {
        return Suite.met(subject).sos(value);
    }

    @Override
    public Subject sos(Object key, Object value) {
        return Suite.met(subject).sos(key, value);
    }

    @Override
    public <B> Subject sen(Class<B> key) {
        return Suite.met(subject).sen(key);
    }

    @Override
    public Subject unset() {
        return Suite.met(subject).unset();
    }

    @Override
    public Subject unset(Object key) {
        return Suite.met(subject).unset(key);
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
    public <B> B gsg(B substitute) {
        return Suite.met(subject).gsg(substitute);
    }

    @Override
    public <B> B gsg(Object key, B substitute) {
        return Suite.met(subject).gsg(key, substitute);
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
        return Suite.met(subject).met(that);
    }

    @Override
    public Subject met(Subject that, Object... keys) {
        return Suite.met(subject).met(that, keys);
    }

    @Override
    public Subject mix(Subject source, Object... keys) {
        return Suite.met(subject).mix(source, keys);
    }

    @Override
    public Stream<Subject> stream() {
        return subject.stream();
    }

    @Override
    public FlowIterator<Subject> iterator() {
        return subject.iterator();
    }

    @Override
    public Subject fuse() {
        return this;
    }
}
