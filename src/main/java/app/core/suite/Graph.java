package app.core.suite;

import app.core.fluid.Fluid;
import app.core.fluid.FluidIterator;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class Graph/* implements Subject */{
//
//    private final Subject sub = Suite.set();
//
//    public Graph set(Object element) {
//        sub.set(element, Suite.set(element));
//        return this;
//    }
//
//    public Graph set(Object key, Object value) {
//        sub.set(key, Suite.set(value));
//        return this;
//    }
//
//    public Graph set(Object from, Object via, Object to) {
//        sub.set(from, Suite.set(via, to));
//        return this;
//    }
//
//    public Graph put(Object element) {
//        sub.getDone(element, Suite::set).put(element);
//        return this;
//    }
//
//    public Graph put(Object key, Object value) {
//        sub.getDone(key, Suite::set).put(value);
//        return this;
//    }
//
//    public Graph put(Object from, Object via, Object to) {
//        sub.getDone(from, Suite::set).put(via, to);
//        return this;
//    }
//
//    @Override
//    public Subject add(Object value) {
//        sub.getSaved(new Object(), Suite.set()).add(value);
//        return this;
//    }
//
//    public Graph add(Object from, Object to) {
//        sub.getDone(from, Suite::set).add(to);
//        return this;
//    }
//
//    public Graph unset() {
//        sub.unset();
//        return this;
//    }
//
//    public Graph unset(Object key) {
//        sub.unset(key);
//        return this;
//    }
//
//    public Graph unset(Object from, Object via) {
//        sub.unset(from, via);
//        return this;
//    }
//
//    @Override
//    public Subject setAll(Iterable<Subject> iterable) {
//        for(Subject it : iterable) {
//
//            subject = subject.set(it.key().direct(), it.direct());
//        }
//        return subject;
//    }
//
//    public Subject get(Object key) {
//
//        Subject s = sub.get(key);
//        return new Graph().setAll(s.key().direct(), s.asExpected());
//    }
//
//    @Override
//    public Subject key() {
//        return sub.key();
//    }
//
//    @Override
//    public Subject prime() {
//        return sub.settled() ? get(sub.prime().key().direct()) : Suite.set();
//    }
//
//    @Override
//    public Subject recent() {
//        return sub.settled() ? get(sub.recent().key().direct()) : Suite.set();
//    }
//
//    @Override
//    public Object direct() {
//        return sub.recent().direct();
//    }
//
//    @Override
//    public <B> B asExpected() {
//        return sub.recent().asExpected();
//    }
//
//    @Override
//    public <B> B asGiven(Class<B> requestedType) {
//        return sub.recent().asGiven(requestedType);
//    }
//
//    @Override
//    public <B> B asGiven(Glass<? super B, B> requestedType) {
//        return sub.recent().asGiven(requestedType);
//    }
//
//    @Override
//    public <B> B asGiven(Class<B> requestedType, B substitute) {
//        return sub.recent().asGiven(requestedType, substitute);
//    }
//
//    @Override
//    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
//        return sub.recent().asGiven(requestedType, substitute);
//    }
//
//    @Override
//    public <B> B orGiven(B substitute) {
//        return sub.recent().orGiven(substitute);
//    }
//
//    @Override
//    public <B> B orDo(Supplier<B> supplier) {
//        return sub.recent().orDo(supplier);
//    }
//
//    @Override
//    public boolean assigned(Class<?> type) {
//        return sub.recent().assigned(type);
//    }
//
//    @Override
//    public boolean settled() {
//        return sub.settled();
//    }
//
//    @Override
//    public int size() {
//        return sub.size();
//    }
//
//    @Override
//    public Stream<Subject> stream() {
//        return sub.stream();
//    }
//
//    @Override
//    public Fluid<Subject> front() {
//        return sub.front();
//    }
//
//    @Override
//    public Fluid<Subject> reverse() {
//        return sub.reverse();
//    }
//
//    @Override
//    public Fluid<Object> values(boolean lastFirst) {
//        return () -> new FluidIterator<Object>() {
//            final FluidIterator<Subject> upper = lastFirst ? sub.reverse().iterator() : sub.front().iterator();
//            FluidIterator<Object> it = upper.hasNext() ?
//                    upper.next().values(lastFirst).iterator() : FluidIterator.empty();
//
//            @Override
//            public boolean hasNext() {
//                if(it.hasNext())return true;
//                while (upper.hasNext()) {
//                    it = upper.next().values(lastFirst).iterator();
//                    if(it.hasNext())return true;
//                }
//                return false;
//            }
//
//            @Override
//            public Object next() {
//                return it.next();
//            }
//        };
//    }
//
//    @Override
//    public Fluid<Object> keys(boolean lastFirst) {
//        return sub.keys(lastFirst);
//    }
}
