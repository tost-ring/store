package app.core.suite;

import app.core.fluid.FluidSubject;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PullSubject implements Subject{

    @Override
    public Subject set(Object element) {
        return set(element, element);
    }

    @Override
    public Subject put(Object element) {
        return put(element, element);
    }

    @Override
    public Subject put(Object key, Object value) {
        return get(key).settled() ? this : set(key, value);
    }

    @Override
    public Subject add(Object element) {
        return set(new Suite.Add(), element);
    }

    @Override
    public Subject unset(Object key, Object value) {
        return get(key).direct().equals(value) ? unset(key) : this;
    }

    @Override
    public Subject key() {
        return prime().key();
    }

    @Override
    public Subject prime() {
        Iterator<Subject> it = front().iterator();
        return it.hasNext() ? it.next() : Suite.set();
    }

    @Override
    public Subject recent() {
        Iterator<Subject> it = reverse().iterator();
        return it.hasNext() ? it.next() : Suite.set();
    }

    @Override
    public Object direct() {
        return prime().direct();
    }

    @Override
    public <B> B asExpected() {
        return prime().asExpected();
    }

    @Override
    public <B> B asGiven(Class<B> requestedType) {
        return prime().asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType) {
        return prime().asGiven(requestedType);
    }

    @Override
    public <B> B asGiven(Class<B> requestedType, B substitute) {
        return prime().asGiven(requestedType, substitute);
    }

    @Override
    public <B> B asGiven(Glass<? super B, B> requestedType, B substitute) {
        return prime().asGiven(requestedType, substitute);
    }

    @Override
    public <B> B orGiven(B substitute) {
        return prime().orGiven(substitute);
    }

    @Override
    public <B> B orDo(Supplier<B> supplier) {
        return prime().orDo(supplier);
    }

    @Override
    public boolean assigned(Class<?> type) {
        return prime().assigned(type);
    }

    @Override
    public boolean settled() {
        return prime().settled();
    }

    @Override
    public Subject iterable() {
        return this;
    }

    @Override
    public Subject getSaved(Object key, Object reserve) {
        Subject saved = get(key);
        if(saved.settled())return saved;
        set(key, reserve);
        return get(key);
    }

    @Override
    public Subject getDone(Object key, Supplier<?> supplier) {
        Subject done = get(key);
        if(done.settled())return done;
        set(key, supplier.get());
        return get(key);
    }

    @Override
    public Subject getDone(Object key, Function<Subject, ?> function, Subject argument) {
        Subject done = get(key);
        if(done.settled())return done;
        set(key, function.apply(argument));
        return get(key);
    }

    @Override
    public Subject take(Object key) {
        Subject taken = get(key);
        unset(key);
        return taken;
    }

    @Override
    public boolean homogeneous() {
        return true;
    }

    @Override
    public abstract FluidSubject front();

    @Override
    public abstract Subject unset();

    @Override
    public abstract FluidSubject reverse();
}
