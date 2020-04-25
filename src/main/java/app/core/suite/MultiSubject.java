package app.core.suite;

import app.core.fluid.*;

import java.util.*;
import java.util.function.Supplier;

class MultiSubject implements Subject {

    private final Chain chain;

    MultiSubject() {
        this.chain = new Chain();
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
    public Subject put(Object element) {
        return put(element, element);
    }

    @Override
    public Subject put(Object key, Object value) {
        chain.putIfAbsent(key, value);
        return this;
    }

    @Override
    public Subject add(Object element) {
        chain.put(new Object(), element);
        return this;
    }

    @Override
    public Subject unset(Object key) {
        chain.remove(key);
        return this;
    }

    @Override
    public Subject unset(Object key, Object value) {
        chain.remove(key, value);
        return this;
    }

    @Override
    public Subject key() {
        var link = chain.getFirstLink();
        return link == null ? ZeroSubject.getInstance() : link.key();
    }

    @Override
    public Subject prime() {
        var link = chain.getFirstLink();
        return link == null ? ZeroSubject.getInstance() : link;
    }

    @Override
    public Subject recent() {
        var link = chain.getLastLink();
        return link == null ? ZeroSubject.getInstance() : link;
    }

    @Override
    public Subject get(Object key) {
        var link = chain.getLink(key);
        return link == null ? ZeroSubject.getInstance() : link;
    }

    @Override
    public Object direct() {
        var link = chain.getFirstLink();
        return link == null ? null : link.direct();
    }
    
    @Override
    public <B> B asExpected() {
        var link = chain.getFirstLink();
        if(link == null) throw new NoSuchElementException();
        return link.asExpected();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        var link = chain.getFirstLink();
        if(link == null) throw new NoSuchElementException();
        return link.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        var link = chain.getFirstLink();
        if(link == null) throw new NoSuchElementException();
        return link.asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B reserve) {
        var link = chain.getFirstLink();
        if(link == null)return reserve;
        return link.asGiven(requestedType, reserve);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B reserve) {
        var link = chain.getFirstLink();
        if(link == null)return reserve;
        return link.asGiven(requestedType);
    }

    @Override
    public <B> B orGiven(B reserve) {
        var link = chain.getFirstLink();
        if(link == null)return reserve;
        return link.orGiven(reserve);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        var link = chain.getFirstLink();
        if(link == null)return supplier.get();
        return link.orDo(supplier);
    }

    @Override
    public boolean assigned(Class<?> type) {
        var link = chain.getFirstLink();
        if(link == null)return false;
        return link.assigned(type);
    }

    @Override
    public boolean settled() {
        return !chain.isEmpty();
    }

    @Override
    public int size() {
        return chain.size();
    }

    @Override
    public FluidSubject front() {
        return chain;
    }

    @Override
    public FluidSubject reverse() {
        return () -> chain.iterator(true);
    }

    @Override
    public String toString() {
        return chain.toString();
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
