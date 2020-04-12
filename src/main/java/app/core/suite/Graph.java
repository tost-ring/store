package app.core.suite;

import app.core.flow.FlowIterable;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class Graph implements Subject {

    private Subject sub = Suite.set();

    public Graph set(Object node) {
        sub.set(node, Suite.set());
        return this;
    }

    public Graph set(Object from, Object viaTo) {
        sub.set(from, Suite.set(viaTo));
        return this;
    }

    public Graph set(Object from, Object via, Object to) {
        sub.set(from, Suite.set(via, to));
        return this;
    }

    public Graph put(Object node) {
        sub.put(node, Suite.set());
        return this;
    }

    public Graph put(Object from, Object viaTo) {
        sub.getDone(from, Suite::set).asGiven(Subject.class).put(viaTo);
        return this;
    }

    public Graph put(Object from, Object via, Object to) {
        sub.getDone(from, Suite::set).asGiven(Subject.class).put(via, to);
        return this;
    }

    @Override
    public Subject add(Object element) {
        throw new UnsupportedOperationException();
    }

    public Graph add(Object from, Object to) {
        sub.getDone(from, Suite::set).asGiven(Subject.class).add(to);
        return this;
    }

    public Graph unset() {
        sub.unset();
        return this;
    }

    public Graph unset(Object node) {
        sub.unset(node);
        return this;
    }

    public Graph unset(Object from, Object via) {
        sub.unset(from, via);
        return this;
    }

    public Subject get(Object key) {
        return sub.get(key).orDo(Suite::set);
    }

    @Override
    public Subject key() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Subject prime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Subject recent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object direct() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B asExpected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B substitute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B orGiven(B substitute) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isIn(Class<?> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean settled() {
        return sub.settled();
    }

    @Override
    public int size() {
        return sub.size();
    }

    @Override
    public Stream<Subject> stream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlowIterable<Subject> front() {
        return sub.front().map(s -> Suite.set(s.key().direct(), s.asGiven(Subject.class).direct()));
    }

    @Override
    public FlowIterable<Subject> reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlowIterable<Object> values(boolean lastFirst) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FlowIterable<Object> keys(boolean lastFirst) {
        return sub.keys(lastFirst);
    }
}
